package codigo.labplc.mx.trackxi.expresionesregulares;

import java.util.regex.Pattern;

public class RegularExpressions {
	public static final int KEY_IS_STRING = 1;
	public static final int KEY_IS_NUMBER = 2;
	public static final int KEY_IS_EMAIL = 3;
	public static final int KEY_IS_NICKNAME = 4;
	
	private static String IS_NUMBER = "(\\d+)";
	private static String IS_EMAIL = "^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static String IS_STRING = "(\\D+)";
	private static String IS_NICKNAME = "[A-Za-z0-9._-]+";
	
	public static boolean isNumber(String expression) {
		return evaluateExpression(IS_NUMBER, expression);
	}
	
	public static boolean isNickName(String expression) {
		return evaluateExpression(IS_NICKNAME, expression);
	}
	
	public static  boolean isEmail(String expression) {
		return evaluateExpression(IS_EMAIL, expression);
	}
	
	public static  boolean isString(String expression) {
		return evaluateExpression(IS_STRING, expression);
	}
	
	private static boolean evaluateExpression(String regularExpression, String expression) {
		boolean match = Pattern.matches(regularExpression, expression);
		
		if (match) {
			return true;
		} else {
			return false;
		}
	}
}