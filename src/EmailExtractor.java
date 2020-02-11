import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailExtractor {
    URL url; // url for webpage.
    StringBuilder html; // contents of webpage.
    // pattern found at https://www.freeformatter.com/java-regex-tester.html
    String pattern = "[-a-z0-9~!$%^&*_=+}{'?]+(\\.[-a-z0-9~!$%^&*_=+}{'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?";
    Set<String> emails = new HashSet<>(); // hashset; prevents duplicate emails.
    int responseNum;
    public EmailExtractor(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            responseNum = 2;
        }
    }

    public void extract() {
        if(readPage()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(html);
            while (m.find())
                emails.add(m.group());
            if (emails.isEmpty())
                responseNum = 1;
            else
                responseNum = 0;
        }
    }

    public boolean readPage() {
        try(BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()))) {
            html = new StringBuilder();
            String in;
            while((in = read.readLine()) != null)
                html.append(in);
        } catch (Exception e) {
            responseNum = 2;
            return false;
        }
        return true;
    }

    public void flushEmails() {
        this.emails.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String s : emails)
            sb.append(s).append("\n");
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}