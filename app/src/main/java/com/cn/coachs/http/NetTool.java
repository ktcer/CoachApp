package com.cn.coachs.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import android.os.Environment;

import com.cn.coachs.util.CreateFolder;

/**
 * 所有的网络请求都封装在这里
 *
 * @author kuangtiecheng
 */
public class NetTool {
    private static final int TIMEOUT = 10000;// 10秒
    private static String JSPSESSID = null;
    private static String PHONE = null;

    /**
     * 多文件上传 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能: <FORM METHOD=POST
     * ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet"
     * enctype="multipart/form-data"> <INPUT TYPE="text" NAME="name"> <INPUT
     * TYPE="text" NAME="id"> <input type="file" name="imagefile"/> <input
     * type="file" name="zip"/> </FORM>
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
     *               www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file   上传文件
     */
    public static String post(String path, Map<String, String> params,
                              FormFile[] files) throws Exception {
        final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志

        int fileDataLength = 0;
        for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
            StringBuilder fileExplain = new StringBuilder();
            fileExplain.append("--");
            fileExplain.append(BOUNDARY);
            fileExplain.append("\r\n");
            fileExplain.append("Content-Disposition: form-data;name=\""
                    + uploadFile.getParameterName() + "\";filename=\""
                    + uploadFile.getFilname() + "\"\r\n");
            fileExplain.append("Content-Type: " + uploadFile.getContentType()
                    + "\r\n\r\n");
            fileExplain.append("\r\n");
            fileDataLength += fileExplain.length();
            if (uploadFile.getInStream() != null) {
                fileDataLength += uploadFile.getFile().length();
            } else {
                fileDataLength += uploadFile.getData().length;
            }
        }
        StringBuilder textEntity = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
            textEntity.append("--");
            textEntity.append(BOUNDARY);
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"\r\n\r\n");
            textEntity.append(entry.getValue());
            textEntity.append("\r\n");
        }
        // 计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length
                + fileDataLength + endline.getBytes().length;

        URL url = new URL(path);
        int port = url.getPort() == -1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        // socket.setSoTimeout(2*TIMEOUT);
        OutputStream outStream = socket.getOutputStream();
        // 下面完成HTTP请求头的发送

        String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary="
                + BOUNDARY + "\r\n";
        outStream.write(contenttype.getBytes());

        String contentlength = "Content-Length: " + dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        // Cookie
        if (JSPSESSID != null && PHONE != null) {
            String Cookie = "Cookie: JSPSESSID=" + JSPSESSID + ";" + "PHONE="
                    + PHONE + "\r\n";
            outStream.write(Cookie.getBytes());
        }
        String host = "Host: " + url.getHost() + ":" + port + "\r\n";
        outStream.write(host.getBytes());
        // 写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        // 把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        // 把所有文件类型的实体数据发送出来
        for (FormFile uploadFile : files) {
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\""
                    + uploadFile.getParameterName() + "\";filename=\""
                    + uploadFile.getFilname() + "\"\r\n");
            fileEntity.append("Content-Type: " + uploadFile.getContentType()
                    + "\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            if (uploadFile.getInStream() != null) {
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                uploadFile.getInStream().close();
            } else {
                outStream.write(uploadFile.getData(), 0,
                        uploadFile.getData().length);
            }
            outStream.write("\r\n".getBytes());
        }
        // 下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());
        String s = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        if (reader.readLine().indexOf("200") == -1) {// 读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
            return "send File Error";
        } else {
            String line;
            while ((line = reader.readLine()) != null) {
                s += line;
                if (line.contains("}")) {
                    break;
                }
            }
        }
        outStream.flush();
        outStream.close();
        reader.close();
        socket.close();
        String ns = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
        return ns;
    }

    /**
     * 传送文本,例如Json,xml等
     */
    public static String sendTxt(String urlPath, String txt, String encoding)
            throws Exception {
        byte[] sendData = txt.getBytes();
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(TIMEOUT);
        // 如果通过post提交数据，必须设置允许对外输出数据
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "text/xml");
        conn.setRequestProperty("Charset", encoding);
        conn.setRequestProperty("Content-Length",
                String.valueOf(sendData.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(sendData);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            // 获得服务器响应的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), encoding));
            // 数据
            String retData = null;
            String responseData = "";
            while ((retData = in.readLine()) != null) {
                responseData += retData;
            }
            in.close();
            return responseData;
        }
        return "sendText error!";
    }

    /**
     * 上传文件
     *
     * @param urlPath
     * @param ID
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String uploadFile(String urlPath, String filePath,
                                    Map<String, String> headers, List<String> params) throws Exception {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        URL url = new URL(urlPath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        /* 允许Input、Output，不使用Cache */
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        /* 设置传送的method=POST */
        con.setRequestMethod("POST");
		/* setRequestProperty */

        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                + boundary); // form-data表明上传的是表单数据

        if (headers != null) {
            Set<Entry<String, String>> es = headers.entrySet();
            for (Entry<String, String> e : es) {
                con.setRequestProperty(e.getKey(), e.getValue());
            }
        }

        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {

            for (int i = 0; i < params.size(); i++) {
                String t = params.get(i);
                sb.append("--" + boundary + "\r\n");
                sb.append("Content-Disposition: form-data; name=\"" + t + "\""
                        + "\r\n");
                sb.append("\r\n");
                sb.append(t + "\r\n");
            }

        }

        DataOutputStream outStream = new DataOutputStream(con.getOutputStream());
        File f = new File(filePath);
        try {
            if (sb.length() > 0) {
                outStream.writeBytes(sb.toString());
            }
			/* 设置DataOutputStream */
            outStream.writeBytes(twoHyphens + boundary + end);
            outStream.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file\";filename=\"" + f.getName() + "\"" + end);

            outStream.writeBytes(end);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

		/* 取得文件的FileInputStream */
        FileInputStream fStream = new FileInputStream(filePath);
		/* 设置每次写入1024bytes */
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int length = -1;
		/* 从文件读取数据至缓冲区 */
        while ((length = fStream.read(buffer)) != -1) {
			/* 将资料写入DataOutputStream中 */
            outStream.write(buffer, 0, length);
        }

        outStream.writeBytes(end);
        outStream.writeBytes(twoHyphens + boundary + twoHyphens + end);

		/* close streams */
        fStream.close();
        outStream.flush();

		/* 取得Response内容 */
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b = new StringBuffer();
        while ((ch = is.read()) != -1) {
            b.append((char) ch);
        }
		/* 关闭DataOutputStream */
        outStream.close();
        String s = b.toString();
        String ns = new String(s.getBytes("iso-8859-1"), "utf-8");
        return ns;
    }

    /**
     * 上传文件
     *
     * @param urlPath
     * @param ID
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String uploadFile(String urlPath, String filePath,
                                    Map<String, String> headers) throws Exception {
        return uploadFile(urlPath, filePath, headers, null);
    }

    /**
     * 通过get方式提交参数给服务器
     */
    public static String sendGetRequest(String urlPath,
                                        Map<String, String> params, String encoding) throws Exception {
        params.put("deviceType", "android");
        // 使用StringBuilder对象
        StringBuilder sb = new StringBuilder(urlPath);
        sb.append('?');

        // 迭代Map
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append('=')
                    .append(URLEncoder.encode(entry.getValue(), encoding))
                    .append('&');
        }
        sb.deleteCharAt(sb.length() - 1);
        // 打开链接
        URL url = new URL(sb.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "text/xml");
        conn.setRequestProperty("Charset", encoding);
        conn.setConnectTimeout(TIMEOUT);
        // 如果请求响应码是200，则表示成功
        if (conn.getResponseCode() == 200) {
            // 获得服务器响应的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), encoding));
            // 数据
            String retData = null;
            String responseData = "";
            while ((retData = in.readLine()) != null) {
                responseData += retData;
            }
            in.close();
            return responseData;
        }
        return "sendGetRequest error!";

    }

    /**
     * 通过Post方式提交参数给服务器,也可以用来传送json或xml文件
     */
    public static String sendPostRequest(String urlPath,
                                         Map<String, String> params, String encoding) throws Exception {
        params.put("deviceType", "android");
        StringBuilder sb = new StringBuilder();
        // 如果参数不为空
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // Post方式提交参数的话，不能省略内容类型与长度
                sb.append(entry.getKey()).append('=')
                        .append(URLEncoder.encode(entry.getValue(), encoding))
                        .append('&');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        // 得到实体的二进制数据
        byte[] entitydata = sb.toString().getBytes();
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(TIMEOUT);
        // 如果通过post提交数据，必须设置允许对外输出数据
        conn.setDoOutput(true);
        // 这里只设置内容类型与内容长度的头字段
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        // conn.setRequestProperty("Content-Type", "text/xml");
        conn.setRequestProperty("Charset", encoding);
        conn.setRequestProperty("Content-Length",
                String.valueOf(entitydata.length));
        OutputStream outStream = conn.getOutputStream();
        // 把实体数据写入是输出流
        outStream.write(entitydata);
        // 内存中的数据刷入
        outStream.flush();
        outStream.close();
        // 如果请求响应码是200，则表示成功
        if (conn.getResponseCode() == 200) {
            // 获得服务器响应的数据
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), encoding));
            // 数据
            String retData = null;
            String responseData = "";
            while ((retData = in.readLine()) != null) {
                responseData += retData;
            }
            in.close();
            return responseData;
        }
        return "sendText error!";
    }

    /**
     * 在遇上HTTPS安全模式或者操作cookie的时候使用HTTPclient会方便很多 使用HTTPClient（开源项目）向服务器提交参数
     */
    public static String sendHttpClientPost(String urlPath,
                                            Map<String, String> params, String encoding) throws Exception {
        // 需要把参数放到NameValuePair
        List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();
        params.put("deviceType", "android");
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramPairs.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
        }
        // 对请求参数进行编码，得到实体数据
        UrlEncodedFormEntity entitydata = new UrlEncodedFormEntity(paramPairs,
                encoding);
        // 构造一个请求路径
        HttpPost post = new HttpPost(urlPath);
        if (JSPSESSID != null && PHONE != null) {
            post.setHeader("Cookie", "JSPSESSID=" + JSPSESSID + ";" + "PHONE="
                    + PHONE);
        }
        // 设置请求实体
        post.setEntity(entitydata);
        // 浏览器对象
        DefaultHttpClient client = new DefaultHttpClient();
        // 请求超时
        client.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                TIMEOUT);
        // 执行post请求
        HttpResponse response = client.execute(post);
        // 从状态行中获取状态码，判断响应码是否符合要求
        if (response.getStatusLine().getStatusCode() == 200) {
            getCookie(client);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, encoding);
            BufferedReader reader = new BufferedReader(inputStreamReader);// 读字符串用的。
            String s;
            String responseData = "";
            while (((s = reader.readLine()) != null)) {
                responseData += s;
            }
            reader.close();// 关闭输入流
            return responseData;
        }
        return "sendHttpClientPost error!";
    }

    private static void getCookie(DefaultHttpClient httpClient) {
        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        // for (int i = 0; i < cookies.size(); i++) {
        // Cookie cookie = cookies.get(i);
        // String cookieName = cookie.getName();
        // String cookieValue = cookie.getValue();
        // if (!TextUtils.isEmpty(cookieName)
        // && !TextUtils.isEmpty(cookieValue)) {
        // // StringBuffer sb = new StringBuffer();
        // // sb.append(cookieName + "=");
        // // sb.append(cookieValue + ";");
        // // Log.i("cookie", sb.toString());
        // }
        // }

        for (int i = 0; i < cookies.size(); i++) {
            // 这里是读取Cookie['JSPSESSID']的值存在静态变量中，保证每次都是同一个值
            if ("JSPSESSID".equals(cookies.get(i).getName())) {
                JSPSESSID = cookies.get(i).getValue();
            }
            if ("PHONE".equals(cookies.get(i).getName())) {
                PHONE = cookies.get(i).getValue();
            }
        }
    }

    /**
     * 根据URL直接读文件内容，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容
     */
    public static String readTxtFile(String urlStr, String encoding)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {
            // 创建一个URL对象
            URL url = new URL(urlStr);
            // 创建一个Http连接
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            // 使用IO流读取数据
            buffer = new BufferedReader(new InputStreamReader(
                    urlConn.getInputStream(), encoding));
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
     */
    public static int downloadFile(String urlStr, String path, String fileName)
            throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = getInputStreamFromUrl(urlStr);
            File resultFile = write2SDFromInput(path, fileName, inputStream);
            if (resultFile == null) {
                return -1;
            }

        } catch (Exception e) {
            return -1;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                throw e;
            }
        }
        return 0;
    }

    /**
     * 该函数返回整形 -1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
     */
    public static int downloadFile1(String urlStr, String path, String fileName)
            throws Exception {
        String getFileName = CreateFolder.getFileNameInFolder(path, fileName);
        if (getFileName.equals(fileName)) {
            return 1;
        }
        InputStream inputStream = null;
        try {
            inputStream = getInputStreamFromUrl1(urlStr);
            File resultFile = write2SDFromInput1(path, fileName, inputStream);
            if (resultFile == null) {
                return -1;
            }

        } catch (Exception e) {
            return -1;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                throw e;
            }
        }
        return 0;
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static InputStream getInputStreamFromUrl(String urlStr)
            throws MalformedURLException, IOException {
        URL url = new URL(urlStr);// 链接有中文要设成utf-8才能下载哦
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        // urlConn.setRequestProperty("Charset", "UTF-8");
        InputStream inputStream = urlConn.getInputStream();
        return inputStream;
    }

    public static InputStream getInputStreamFromUrl1(String urlStr)
            throws MalformedURLException, IOException {
        String docname = urlStr.substring(urlStr.lastIndexOf("/") + 1);

        urlStr = urlStr.substring(0, urlStr.lastIndexOf("/")) + "/"
                + URLEncoder.encode(docname, "utf-8");
        URL url = new URL(urlStr);// 链接有中文要设成utf-8才能下载哦
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        // urlConn.setRequestProperty("Charset", "UTF-8");
        InputStream inputStream = urlConn.getInputStream();
        return inputStream;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    private static File write2SDFromInput(String directory, String fileName,
                                          InputStream input) {
        File file = null;
        String SDPATH = Environment.getExternalStorageDirectory().toString();
        FileOutputStream output = null;
        File dir = new File(SDPATH + directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            file = new File(dir + File.separator + fileName);
            file.createNewFile();
            output = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    private static File write2SDFromInput1(String directory, String fileName,
                                           InputStream input) {
        File file = null;
        // String SDPATH = Environment.getExternalStorageDirectory().toString();
        FileOutputStream output = null;
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            file = new File(dir + File.separator + fileName);
            file.createNewFile();
            output = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}