using implementation.Model;

namespace implementation.Utils;

public class HttpUtils
{
    public static readonly int HTTP_PORT = 80; // default HTTP port is port 80

    // Splits the response into header and body
    public static string GetResponseBody(string responseContent)
    {
        var responseParts = responseContent.Split(new[] { "\r\n\r\n" }, StringSplitOptions.RemoveEmptyEntries);
        return responseParts.Length > 1 ? responseParts[1] : "";
    }

    // Checks if the response header has been fully obtained
    public static bool ResponseHeaderFullyObtained(string responseContent)
    {
        return responseContent.Contains("\r\n\r\n");
    }

    public static int GetContentLength(string responseContent)
    {
        // get the content length from the response headers
        var responseLines = responseContent.Split('\r', '\n');
        foreach (var responseLine in responseLines)
        {
            // split the response header into header name and value
            var headerDetails = responseLine.Split(':');
            if (headerDetails[0].CompareTo("Content-Length") == 0)
            {
                // return the content length
                return int.Parse(headerDetails[1]);
            }
        }

        return 0;
    }

    // Creates the request string
    public static string GetRequestString(string hostname, string endpoint)
    {
        return "GET " + endpoint + " HTTP/1.1\r\n" +
               "Host: " + hostname + "\r\n" +
               "Content-Length: 0\r\n\r\n" +
               "Content-Type: text/html";
    }

    // Prints the response to the console
    public static void PrintResponse(StateObject state)
    {
        foreach (var i in state.responseContent.ToString().Split('\r', '\n'))
        {
            Console.WriteLine(i);
        }
    }
}