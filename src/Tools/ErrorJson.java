package Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorJson {
	public static JSONObject serviceRefused(String message, int codeErreur) throws JSONException {
		JSONObject o = new JSONObject();
		o.put("message", message);
		o.put("code", codeErreur);
		return o;

	}

	public static JSONObject serviceAccepted() throws JSONException {
		JSONObject o = new JSONObject();
		o.put("message", "OK");
		o.put("code", 200);
		return o;
	}

}
