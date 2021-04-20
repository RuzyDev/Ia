package br.com.arcom.signpad.connection;

import android.net.SSLCertificateSocketFactory;
import android.util.Log;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import br.com.arcom.signpad.ssl.SSLContextFactory;
import br.com.arcom.signpad.util.ConstantesUtils;

/**
 * @author Luiz Fernando
 * @since 24/09/2018
 *
 */
public class RestSSLConnection {
	
	private SSLContext sslContext;
			
    /** The map request property. */
    private Map<String, String> mapRequestProperty;
    
    /** The param builder. */
    private StringBuilder paramBuilder;
   
    /** The response code. */
    private int responseCode;
    
    /** The url conn. */
    private String urlConn;

    private int connectTimeout;
    
    private int readTimeout;    

       
   /* public RestSSLConnection(AuthenticationParameters authParams) throws Exception {
		 File clientCertFile = authParams.getClientCertificate();

	     //this.sslContext = SSLContextFactory.getInstance().makeContext(clientCertFile, authParams.getClientCertificatePassword(), authParams.getCaCertificate());
	     
	     this.sslContext = SSLContextFactory.getInstance().makeContext(authParams.getCaCertificate());
	}*/
    
    
    public RestSSLConnection(String urlConn, String caCertString, String title) throws Exception {
    	this.urlConn = urlConn;
	    this.sslContext = SSLContextFactory.getInstance().makeContext(caCertString);
	}
    
   
	/**
     * Adds the body param.
     *
     * @param param the param
     */
    public void addBodyParam(String param) {
        if (this.paramBuilder == null) {
            this.paramBuilder = new StringBuilder();
        }
        this.paramBuilder.append(param);
    }
   

    /**
     * Adds the request property.
     *
     * @param key the key
     * @param value the value
     */
    public void addRequestProperty(String key, String value) {
        if (this.mapRequestProperty == null) {
            this.mapRequestProperty = new HashMap<String, String>();
        }
        if (key != null) {
            this.mapRequestProperty.put(key, value);
        }
    }
        
    public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
     * @return
     * @throws Exception
     * <p><b>Autoria: </b>Luiz Fernando - 24/09/2018</p>
     */
    public String doGet() throws Exception {
    	String result;
        HttpURLConnection connection = null;
        
        try{
	        Log.i(ConstantesUtils.TAG_LOG_SIGNPAD,"RestSSLConnection.connect " + urlConn);
	        connection = (HttpURLConnection) new URL(this.urlConn).openConnection();
	        
	        /*if(connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            } */    
	        
	        if (connection instanceof HttpsURLConnection) {
	            HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
	            httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
	            httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
	        }
	        
	 
	        connection.setRequestMethod("GET"); 
	        
	        connection.setConnectTimeout(this.getConnectTimeout()==0?30000:this.getConnectTimeout());
	        connection.setReadTimeout(this.getReadTimeout()==0?30000:this.getReadTimeout());
	        
	        if (this.mapRequestProperty == null || this.mapRequestProperty.isEmpty()) {
	            connection.setRequestProperty("Content-Type", "application/json");
	        } else {
	            for (String key2 : this.mapRequestProperty.keySet()) {
	                connection.addRequestProperty(key2, (String) this.mapRequestProperty.get(key2));
	            }
	        }
	        
	        if (this.paramBuilder != null) {
	            OutputStream os = connection.getOutputStream();
	            os.write(this.paramBuilder.toString().getBytes());
	            os.flush();
	            os.close();
	        }
	        
	        this.responseCode = connection.getResponseCode();
	        Log.i(ConstantesUtils.TAG_LOG_SIGNPAD,"connection.getResponseCode()= " + this.responseCode);
	       
	        try{
            	result = readFully(connection.getInputStream());
            }catch(IOException e){
            	result = readFully(connection.getErrorStream());
            }      
	       
        	
	    } catch(Exception ex) {
	        result = ex.toString();
	    } finally {
	        if(connection != null) {
	        	connection.disconnect();
	        }
	    }
	    return result;
    }
    
    
    /**
     * @return
     * @throws Exception
     * <p><b>Autoria: </b>Luiz Fernando - 24/09/2018</p>
     */
    public String doPost() throws Exception {
        String result;
        HttpURLConnection connection = null;
        
        try{
	        Log.i(ConstantesUtils.TAG_LOG_SIGNPAD,"RestSSLConnection.connect " + urlConn);
	        connection = (HttpURLConnection) new URL(this.urlConn).openConnection();
	        
	       /* if(connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }*/
	        // Erro 14/01/2019 java.security.cert.CertPathValidatorException: Trust anchor for certification path not found 
	        // Por enquanto estou disabilitando HttpURLConnection SSL checking
	        // https://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https/6378872#6378872
	        if (connection instanceof HttpsURLConnection) {
	            HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
	            httpsConn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
	            httpsConn.setHostnameVerifier(new AllowAllHostnameVerifier());
	        }
	       
	        connection.setInstanceFollowRedirects(false);       
	        connection.setDoOutput(true);		
	        connection.setRequestMethod("POST");     
	        
	        connection.setConnectTimeout(this.getConnectTimeout()==0?30000:this.getConnectTimeout());
	        connection.setReadTimeout(this.getReadTimeout()==0?30000:this.getReadTimeout());
	        
	        if (this.mapRequestProperty == null || this.mapRequestProperty.isEmpty()) {
	            connection.setRequestProperty("Content-Type", "application/json");
	        } else {
	            for (String key2 : this.mapRequestProperty.keySet()) {
	                connection.addRequestProperty(key2, (String) this.mapRequestProperty.get(key2));
	            }
	        }
	        
	        if (this.paramBuilder != null) {
	            OutputStream os = connection.getOutputStream();
	            os.write(this.paramBuilder.toString().getBytes());
	            os.flush();
	            os.close();
	        }
	        
	        this.responseCode = connection.getResponseCode();
	        Log.i(ConstantesUtils.TAG_LOG_SIGNPAD,"connection.getResponseCode()= " + this.responseCode);
	       
	        try{
            	result = readFully(connection.getInputStream());
            }catch(IOException e){
            	result = readFully(connection.getErrorStream());
            }      
	       
        	
	    } catch(Exception ex) {
	        result = ex.toString();
	    } finally {
	        if(connection != null) {
	        	connection.disconnect();
	        }
	    }
	    return result;
    }
    
    
    /**
     * Gets the response code.
     *
     * @return the response code
     */
    public int getResponseCode() {
        return this.responseCode;
    }

	public String readFully(InputStream inputStream) throws IOException {

		if(inputStream == null) {
			return "";
		}

		BufferedInputStream bufferedInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;

		try {
			bufferedInputStream = new BufferedInputStream(inputStream);
			byteArrayOutputStream = new ByteArrayOutputStream();

			final byte[] buffer = new byte[1024];
			int available = 0;

			while ((available = bufferedInputStream.read(buffer)) >= 0) {
				byteArrayOutputStream.write(buffer, 0, available);
			}

			return byteArrayOutputStream.toString();

		} finally {
			if(bufferedInputStream != null) {
				bufferedInputStream.close();
			}
		}
	}
}