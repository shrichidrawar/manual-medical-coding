package com.mmc.nts.utility;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mmc.nts.constants.Constants;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.text.Format;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.GregorianCalendar;
import org.apache.commons.lang3.StringUtils;

public class DateUtil {

	public static Date getDateTimeStamp() {
		return java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}
	public static String getFormattedDateTimeStamp() {
		Format f = new SimpleDateFormat(Constants.DATE_FROMAT_YYYY_MM_DD_HH_MM_SS);
		String date = f.format(getDateTimeStamp());
		System.out.println(date);
		return date;
	}
	public static Date stringToDate(String date) throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat(Constants.DATE_FROMAT_YYYY_MM_DD_HH_MM_SS);
		Date formattedDate = f.parse(date);
		System.out.println(formattedDate);
		return formattedDate;
	}
	public static void main(String[] args) throws ParseException, InterruptedException {
		Double pageLevelConfidenceSum = 2D;
		if (pageLevelConfidenceSum < -1D) {
			pageLevelConfidenceSum = -1D;
		}
		System.out.println(pageLevelConfidenceSum);
	}
	public static Date getDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FROMAT_YYYY_MM_DD);
		String date = sdf.format(new Date());
		return sdf.parse(date);
	}
	public static Date getDate(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FROMAT_YYYY_MM_DD);
		String formattedDate = sdf.format(date);
		return sdf.parse(formattedDate);
	}
	public static Date formatDate(Date date, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String formattedDate = sdf.format(date);
		return sdf.parse(formattedDate);
	}
	public static String getformatDateString(Date date, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
	public static List<String> getDateList() {
		List<String> dateList = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FROMAT_YYYY_MM_DD);

		Date today = new Date();
		dateList.add(sdf.format(today));
		for (int i = 1; i < 7; i++) {
			dateList.add(sdf.format(subtractDays(today, i)));
		}
		Collections.sort(dateList);
		return dateList;
	}
	public static Date subtractDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, -days);

		return cal.getTime();
	}
	public static Long dateTimeDifference(Date startDate, Date endDate) {
		return endDate.getTime() - startDate.getTime();
	}
	public static long daysBetween(Date startDate, Date endDate) {
		if (null == startDate || null == endDate) {
			return 0;
		}
		long difference = (endDate.getTime() - startDate.getTime()) / 86400000;
		return Math.abs(difference);
	}
	public static long daysBetweenCurrentDate(Date startDate) {
		try {
			long difference = (getDate().getTime() - formatDate(startDate, Constants.DATE_FROMAT_YYYY_MM_DD).getTime())
					/ 86400000;
			return Math.abs(difference);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1L;
	}
	public static Date convertStringToDate(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		Date receivedDate = null;
		try {
			receivedDate = formatter.parse(dateString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return receivedDate;
	}
	public static Long dateTimeDifferenceInSeconds(Date startDate, Date endDate) {
		return ((endDate.getTime() - startDate.getTime()) / 1000);
	}
	public static Date convertStringToDate(String dateString, String format) {
		try {
			if ("dd-mm-yyyy".equalsIgnoreCase(format)) {
				return StringUtils.isNotEmpty(dateString) ? new SimpleDateFormat("dd-mm-yyyy").parse(dateString) : null;
			} else if ("mm-dd-yyyy".equalsIgnoreCase(format)) {
				return StringUtils.isNotEmpty(dateString) ? new SimpleDateFormat("mm-dd-yyyy").parse(dateString) : null;
			} else if ("dd/mm/yyyy".equalsIgnoreCase(format)) {
				return StringUtils.isNotEmpty(dateString) ? new SimpleDateFormat("dd/mm/yyyy").parse(dateString) : null;
			} else if ("mm/dd/yyyy".equalsIgnoreCase(format)) {
				return StringUtils.isNotEmpty(dateString) ? new SimpleDateFormat("mm/dd/yyyy").parse(dateString) : null;
			} else {
				return null;
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	public static String checkValidDate(String dateString, String format) {
		String regex = "";
		boolean result = false;
		if ("dd-mm-yyyy".equalsIgnoreCase(format)) {
			regex = "^(0[1-9]|[12][0-9]|[3][01])-(0[1-9]|1[012])-\\d{4}$";
			result = dateString.matches(regex);
		} else if ("mm-dd-yyyy".equalsIgnoreCase(format)) {
			regex = "^(0[1-9]|1[012])-(0[1-9]|[12][0-9]|[3][01])-\\d{4}$";
			result = dateString.matches(regex);
		} else if ("dd/mm/yyyy".equalsIgnoreCase(format)) {
			regex = "^(0[1-9]|[12][0-9]|[3][01])/(0[1-9]|1[012])/\\d{4}$";
			result = dateString.matches(regex);
		} else if ("mm/dd/yyyy".equalsIgnoreCase(format)) {
			regex = "^(0[1-9]|1[012])/(0[1-9]|[12][0-9]|[3][01])/\\d{4}$";
			result = dateString.matches(regex);
		}
		if (result) {
			return "Valid Date";
		} else {
			return "Invalid Date";
		}
	}
}
