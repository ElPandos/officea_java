/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.other;

/**
 *
 * @author Laptop
 */
public class SmsMsg
{

}

/*
JAVA Code Example

To help you get started using the Enterprise API we provide some sample code in Java.
The code uses JAXP + SAX to generate the XML to make requests to the server but of course you can generate XML in any way.

01
public class Message {
02
 
03
 public static String TELEPHONE_NO = "+4072221525561";
04
 public static String EMAIL = "mail@yahoo.com";
05
 public static String SERVER_URL = "http://tele2se.msgsend.com/mmpNG/ws_xml.html";
06
 public static String APPLICATION_ID = "7721039767285321721";// "7721039767285321721"
07
 public static String SUCCESS = "100";
08
 public static String NOT_ALLOWED = "200";
09
 public static String INVALID_MSISDN = "202";
10
 public static String NO_MSISDN_SPEC = "204";
11
 public static String APPID_ERROR = "206";
12
 
13
 public String _regrCode = null;
14
 public String _pinCode = "3887";
15
 public String _userkey = "16041 13393bb296626177c4ca40a3758463a212c1d";
16
 public String _receiverPhoneNo = "40745518817";
17
 public String _sendMessage = "test sms";
18
 
19
 public String readResponse(String request) throws IOException {
20
  URL url = new URL(SERVER_URL);
21
  URLConnection con = url.openConnection();
22
  con.setDoOutput(true);
23
  DataOutputStream dos = new DataOutputStream(con.getOutputStream());
24
  dos.writeBytes(request);
25
  dos.close();
26
  BufferedReader in = new BufferedReader(new InputStreamReader(con
27
    .getInputStream()));
28
 
29
  String response = "";
30
  String tmp = "";
31
  while ((tmp = in.readLine()) != null) {
32
   response += tmp;
33
  }
34
  in.close();
35
  return response;
36
 
37
 }
  
 /*
== Start Registration ==
<?xml version="1.0" encoding="UTF-8"?>
<register appId="7721039747285326721" phoneNumber="+4612345678" email="mail@yahoo.com"/>
==Response looks like this : ==
<result code="100"></result>
 */
 /*
01
public void startRegister() {
02
 StringBuffer request = new StringBuffer();
03
 request.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?> ").append(
04
   "<register appId=\"").append(APPLICATION_ID).append("\" ")
05
   .append("phoneNumber=\"").append(TELEPHONE_NO).append("\" ")
06
   .append("email=\"").append(EMAIL).append("\"/>");
07
 // .append("</register>");
08
 
09
 try {
10
  System.out.println("Registration request send: "
11
    + request.toString());
12
  System.out.println();
13
  String response = readResponse(request.toString());
14
  String _regrCode = getCodeFromResponse(response);
15
  System.out.println("Register code: " + _regrCode);
16
  System.out.println("Response from MMP at registration request: "
17
    + response);
18
 
19
 } catch (IOException e) {
20
  System.out.println(e.getStackTrace());
21
 }
22
 
23
}
 /*
== Complete Registration ==
<?xml version="1.0" encoding="UTF-8"?>
<sendRegistrationCode appId="7721039747285326721" phoneNumber="+4612345678" code="2708"/>
== Response looks like this:==
<result code="100"><message><![CDATA[1604113293bb2966261f7c4ca40a3758463a212c1a]]></message></result>
 */
 /*
01
public void completeRegister() {
02
 
03
 StringBuffer request = new StringBuffer();
04
 request.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
05
   "<sendRegistrationCode appId=\"").append(APPLICATION_ID)
06
   .append("\" ").append("phoneNumber=\"").append(TELEPHONE_NO)
07
   .append("\" ").append("code=\"").append(_pinCode)
08
   .append("\"/>");
09
 
10
 try {
11
  String response = readResponse(request.toString());
12
  String code = getCodeFromResponse(response);
13
  System.out.println("Response from MMP at registr completition: "
14
    + response);
15
  if (SUCCESS.equals(code)) {
16
   _userkey = getUserkeyFromResponse(response);
17
   System.out.println("Registration successful! Userkey: "
18
     + _userkey);
19
 
20
  }
21
 } catch (IOException e) {
22
  System.out.println(e.getStackTrace());
23
 
24
 }
25
 
26
}
  
 /*
== Send Message ==
<?xml version="1.0" encoding="UTF-8"?>
<sendSMS appId="7721039747285326721">
    <key><![CDATA[1604113293bb2966261f7c4ca40a3758463a212c1a]]></key>
    <text>test sms</text>
    <phoneNumber>+4600011122</phoneNumber>
</sendSMS> 
==Response looks like this:==
  <result code="100"><message><![CDATA[1053816]]></message></result>
 */
 /*
01
public void sendSms() {
02
  StringBuffer request = new StringBuffer();
03
  request.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(
04
    "<sendSMS appId=\"").append(APPLICATION_ID).append("\">")
05
    .append("<key><![CDATA[").append(_userkey).append("]]></key>")
06
    .append("<text>").append(_sendMessage).append("</text>")
07
    .append("<phoneNumber>").append(_receiverPhoneNo).append(
08
      "</phoneNumber>").append("</sendSMS>");
09
  try {
10
   System.out.println("Send SMS request: " + request.toString());
11
   String response = readResponse(request.toString());
12
   String code = getCodeFromResponse(response);
13
   System.out.println("Send message code :" + code);
14
   System.out.println("Response from MMP at sending SMS: " + response);
15
 
16
  } catch (IOException e) {
17
   System.out.println(e.getStackTrace());
18
 
19
  }
20
 
21
 }
22
 
23
 private String getUserkeyFromResponse(String response) {
24
  int start = response.indexOf("CDATA") + 6;
25
  String resp = response.substring(start);
26
  StringTokenizer st = new StringTokenizer(resp, "]");
27
  if (st.hasMoreTokens()) {
28
   return st.nextToken();
29
  }
30
  return null;
31
 }
32
 
33
 private String getCodeFromResponse(String response) {
34
  int start = response.indexOf("code") + 6;
35
  int end = start + 3;
36
 
37
  return response.substring(start, end);
38
 }
39
 
40
 public void register() {
41
  startRegister();
42
  if (_pinCode != null) {
43
   completeRegister();
44
  } else {
45
   System.out.println("Error!");
46
  }
47
 }
48
 public void send() {
49
  if (_userkey != null) {
50
   sendSms();
51
  }
52
 }
53
}


 */
