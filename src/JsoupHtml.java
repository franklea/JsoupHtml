import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.zip.GZIPInputStream;

import javax.print.Doc;
import javax.xml.ws.Response;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.omg.IOP.TAG_CODE_SETS;

import com.sun.jna.Function.PostCallRead;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;





public class JsoupHtml {
	
	public static void print(String str){
		System.out.println(str);
	}
	
	public static void writeFile(String str){
		try {
			File file = new File("163.html");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
	
			out.write(str);
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void printArray(ArrayList<String> arrayList){
		for (int i = 0; i < arrayList.size(); i ++){
			System.out.println(arrayList.get(i));
		}
	}
	
	
	
	public static void testUrl(){
		try {
			Connection conn = Jsoup.connect("http://www.163.com");
			//conn.header("User-Agent", "Dalvik/1.6.0 (Linux; U; Android 4.3; X9077 Build/JLS36C)");
            Document doc = conn.get();
			//writeFile(doc.toString());
	
			//System.out.print(doc.toString());
			ArrayList<String> tags_1 = getTags(doc);
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<String> getTags(Document doc){
		Stack<Element> tmpNode = new Stack<Element>();
		ArrayList<String> tags = new ArrayList<>();
		tmpNode.push(doc.child(0));
		
		while (!tmpNode.isEmpty()){
			Element tmp = tmpNode.pop();
			for (int i = 0; i < tmp.children().size(); i ++){
				tags.add(tmp.child(i).tagName());
				tmpNode.push(tmp.child(i));
			}
		}
		
		printArray(tags);
		print(tags.size() + "");
		return tags;
	}
	
	//get HTML Tags
	public static void testFile(){
		File input = new File("F:/spring-workspace/JsoupHtml/src/test.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			ArrayList<String> tags = getTags(doc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void post1(String myUrl,String val) throws ClientProtocolException, IOException{
	
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(myUrl);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("name", "Jack"));
			nameValuePairs.add(new BasicNameValuePair("age", "18"));
			nameValuePairs.add(new BasicNameValuePair("val", val));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			CloseableHttpResponse response = httpClient.execute(httpPost);
		try {	
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			// do something with the response body
			StringBuilder entityStringBuilder = new StringBuilder();

			if (entity != null) {
				try{
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"),8*1024);
					String line = null;
					while((line = bufferedReader.readLine()) != null){
						entityStringBuilder.append(line+"\n");
					}
					print(entityStringBuilder.toString());
					JSONObject result = new JSONObject(entityStringBuilder.toString());
					String reString = result.getString("response");
					print(reString);
					
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			
			
			EntityUtils.consume(entity);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			response.close();
		}

		
	}
	
	public static void testArray(){
		ArrayList<String> server = new ArrayList<String>();
		server.add("123");
		server.add("345");
		server.add("567");
		for (int i = 0; i< server.size(); i++) {
			print(server.get(i));
		}
	}
	
	
	public static void testOkHttp() throws IOException{
		
		Request request = new Request.Builder().url("http://10.234.128.144:8000/data/").build();
		okhttp3.Response response = client.newCall(request).execute();
		if (response.isSuccessful()){
			String content = response.body().toString();
			//InputStream in = response.body().byteStream();
			
			print(content);
		}
	}
	
	
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static final OkHttpClient client = new OkHttpClient();
	
	public static void testOkHttpPost() throws IOException{
		
		RequestBody body = new FormBody.Builder()
				.add("url","www.baidu.com")
				.add("html","baidu").build();
		Request request = new Request.Builder().url("http://10.234.128.144:8000/data/").post(body).build();
		okhttp3.Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			StringBuilder entityStringBuilder = new StringBuilder();
			print(response.body().toString());
			InputStream inputStream = response.body().byteStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
			String line = null;
			while((line = bufferedReader.readLine()) != null){
				entityStringBuilder.append(line+"\n");
			}
			print(entityStringBuilder.toString());
			JSONObject result = new JSONObject(entityStringBuilder.toString());
			String reString = result.getString("response");
			print(reString);
			
			
			
		} else{
			throw new IOException("Unexpected code " + response);
		}
		
		
	}
	
	public static void main(String[] args) throws IOException{
		//testUrl();
		//testFile();
		/*
		try {
			post("http://10.234.128.144:8000/data/", "hello ,httpclient! ");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	
		//testArray();
		/**
		try {
			testOkHttp();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		**/
		
		testOkHttpPost();
		
	}

}

