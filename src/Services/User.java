package Services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import Tools.ConnexionTools;
import Tools.ErrorJson;
import Tools.UserTools;
import BaseDD.Database;

public class User {

	/**
	 * ********CREATE_USER Creation et insertion d'un utilisateur dans la BD Verifir
	 * que les arguments sont pas null et que login n'existe pas
	 * 
	 * @return Json Object qui est le seviceRefuser en cas d'erreur et
	 *         serviceAccepted sinon Entree : prenom + nom + login + password Sortie
	 *         : {}
	 */
	public static JSONObject createUser(String login, String password, String nom, String prenom, String mail,
			String phoneNumber) {
		JSONObject retour = new JSONObject();
		try {
			try {
				if ((login == null) || (mail == null) || (prenom == null) || (nom == null)) {
					return Tools.ErrorJson.serviceRefused("Pas d'argument", -1);
				}
				if ((phoneNumber == null))
					return Tools.ErrorJson.serviceRefused("Pas d'argument phone", -1);
				if (Tools.ConnexionTools.checkUser(login)) {
					return Tools.ErrorJson.serviceRefused("Login existe deja", 1);
				}

				Tools.UserTools.InsertUser(login, password, nom, prenom, mail, phoneNumber);

				retour = Tools.ErrorJson.serviceAccepted();

			} catch (SQLException s) {
				try {
					return Tools.ErrorJson.serviceRefused(s.getMessage(), -1);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		} catch (JSONException e) {
		}

		return retour;
	}

	/**
	 ************ CONNEXION Entree : login + password Sortie : {id,login,key}
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	public static JSONObject connexion(String login, String password) {
		JSONObject retour = new JSONObject();
		try {
			if ((login == null) || (password == null)) {

				return Tools.ErrorJson.serviceRefused("Pas d'argument", -1);
			}
			if (!ConnexionTools.checkUser(login))
				return Tools.ErrorJson.serviceRefused("User not exists", 1);

			if (!ConnexionTools.checkLogPwd(login, password))
				return Tools.ErrorJson.serviceRefused("Wrong password", 2);
			if (ConnexionTools.UserConnected(login))
				return Tools.ErrorJson.serviceRefused("Utilisateur deja connect", 3);

			String key = UserTools.InsertConnexion(login);

			retour = Tools.ErrorJson.serviceAccepted();
			retour.put("login", login);
			retour.put("key", key);

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				return Tools.ErrorJson.serviceRefused(e.getMessage(), -1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return retour;

	}

	/**
	 ********** DECONNEXION verifer que la cle n'est pas null et que la connexion existe dans
	 * la base de donnee et alors peut se d�connecter
	 * 
	 * @return Json Object qui est le seviceRefuser en cas d'erreur et
	 *         serviceAccepted sinon
	 *
	 *         Entree : key Sortie : {}
	 */
	public static JSONObject deconnexion(String key) {
		JSONObject retour = new JSONObject();

		try {
			if (key == null)
				return Tools.ErrorJson.serviceRefused("Cle connexion invalide", 10);
			if (!ConnexionTools.checkSession(key))
				return Tools.ErrorJson.serviceRefused("Cle connexion inexisatante", 100);
			try {
				Connection connexion = Database.getMySQLConnection();
				Statement statement = connexion.createStatement();
				String query = "DELETE FROM session WHERE session_key='" + key + "';";

				int resultat = statement.executeUpdate(query);
				statement.close();
				connexion.close();
				System.out.println(resultat);
			} catch (SQLException e) {

			}
			retour = Tools.ErrorJson.serviceAccepted();

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return retour;
	}

	/**
	 * **********GetProfil
	 * 
	 * @param key
	 * @return
	 */
	/**
	 * $*******************MODIF IC I KEY PAR LOGIN
	 * 
	 * @param key
	 * @return
	 */

	public static JSONObject getProfil(String key) {
		JSONObject retour = new JSONObject();
		try {
			if (key == null)
				return ErrorJson.serviceRefused("Pas d'argument", -1);
			if (!ConnexionTools.checkSession(key))
				return Tools.ErrorJson.serviceRefused("connexion not existante", 1);
			String login = UserTools.getLogSession(key);
			Connection connexion;
			JSONObject jo = new JSONObject();

			connexion = Database.getMySQLConnection();
			Statement statement = connexion.createStatement();

			String query = "SELECT * FROM user WHERE user_login= '" + login + "' ";
			ResultSet resultat = statement.executeQuery(query);
			while (resultat.next()) {
				System.out.println("nom::" + resultat.getString(3));
				jo.append("state", "OK");
				jo.append("code", 200);
				jo.append("user_id", resultat.getString(1));
				jo.append("nom", resultat.getString(5));// 3
				jo.append("prenom", resultat.getString(4));// 4
				jo.append("mail", resultat.getString(6));// 5
				jo.append("telephone", resultat.getString(6));// 6
				System.out.println("afficher nom  :: " + jo.get("nom"));
				System.out.println("afficher prenom  :: " + jo.get("prenom"));
				System.out.println("afficher mail :: " + jo.get("mail"));
				System.out.println("afficher phone  :: " + jo.get("telephone"));
				System.out.println("afficher user_id  :: " + jo.get("user_id"));
			}
			resultat.close();
			statement.close();
			connexion.close();
			retour = jo;
			return jo;

		} catch (JSONException j) {
			j.printStackTrace();
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return retour;

	}

	/**
	 * 
	 * 
	 * @param login
	 * @return
	 */
	public static JSONObject getUserProfil(String login) {
		JSONObject retour = new JSONObject();
		try {
			Connection connexion;
			JSONObject jo = new JSONObject();

			connexion = Database.getMySQLConnection();
			Statement statement = connexion.createStatement();

			String query = "SELECT * FROM user WHERE user_login= '" + login + "' ";
			ResultSet resultat = statement.executeQuery(query);
			while (resultat.next()) {
				System.out.println("nom::" + resultat.getString(3));
				jo.append("state", "OK");
				jo.append("code", 200);
				jo.append("user_id", resultat.getString(1));
				jo.append("nom", resultat.getString(5));
				jo.append("prenom", resultat.getString(4));
				jo.append("mail", resultat.getString(6));
				jo.append("telephone", resultat.getString(6));
				System.out.println("afficher nom  :: " + jo.get("nom"));
				System.out.println("afficher prenom  :: " + jo.get("prenom"));
				System.out.println("afficher mail :: " + jo.get("mail"));
				System.out.println("afficher phone  :: " + jo.get("telephone"));
				System.out.println("afficher user_id  :: " + jo.get("user_id"));
			}
			resultat.close();
			statement.close();
			connexion.close();
			return jo;

		} catch (JSONException j) {
			j.printStackTrace();
		} catch (SQLException s) {
			s.printStackTrace();
		}

		return retour;
	}

	public static JSONObject nbFriends(String login) {
		JSONObject retour = new JSONObject();
		int id = ConnexionTools.getId(login);
		try {
			if (login == null)

				return ErrorJson.serviceRefused("Pas d'arguments", -1);

			if (!ConnexionTools.checkUser(login))
				return ErrorJson.serviceRefused("login Error", 1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Connection connexion = Database.getMySQLConnection();
			Statement statement = connexion.createStatement();
			String req = "SELECT count(*) FROM friends WHERE id_user1= '" + id + "'";
			ResultSet res = statement.executeQuery(req);
			retour.put("statue", "OK");
			retour.put("code", 200);
			if (res.next())

				retour.put("nbfriends", res.getString(1));
			statement.close();
			connexion.close();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retour;
	}

}
