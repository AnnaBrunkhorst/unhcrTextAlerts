package com.simplilearn.mavenproject;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.*;

public class App {
  //////////////////////////////////////// EDIT AS NEEDED ////////////////////////////////////////
  // Find your Twilio information at twilio.com/user/account
  public static final String ACCOUNT_SID = "ACXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
  public static final String AUTH_TOKEN = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
  public static final PhoneNumber TWILIO_NUMBER = new PhoneNumber("+00000000000");
  
  // Message to be sent to recipients
  public static final String MESSAGE =
      "Input message here!";
  
  // Path to Excel file containing recipient phone numbers
  public static final File EXCEL_SHEET = new File("C:\\XXXXX\\XXXXX.xlsx");
  
  // The column in the Excel sheet (first column with content would be 0) containing the phone
  // numbers
  public static final int COLUMN_WITH_NUMS = 0;
  ////////////////////////////////////////////////////////////////////////////////////////////////

  public static void main(String[] args) throws IOException {
    // Generates list of recipient phone numbers
    List<String> recipients = App.readXLSX(COLUMN_WITH_NUMS);
    // Initiates Twilio
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    // Sends message to each recipient if able, otherwise prints failure message
    for (String recipient : recipients) {
      try {
        // Sends message to recipient
        Message message = Message.creator(new PhoneNumber(recipient), TWILIO_NUMBER,
            MESSAGE).create();
        // Prints recipient number and message sent
        System.out.println("SENT TO: " + recipient + " MESSAGE: " + MESSAGE);
      } catch (Exception e) {
        // Prints failure message
        System.out.println("Message failed to send to " + recipient);
      }
    }
  }

  // Reads an Excel file (2007 or newer, .xlsx) given number of column containing phone numbers and
  // returns list of numbers
  public static List<String> readXLSX(int COLUMN_WITH_NUMS) throws IOException {
    // Obtains input bytes from a file
    FileInputStream fis = new FileInputStream(EXCEL_SHEET);
    // Creates workbook instance that refers to .xlsx file
    XSSFWorkbook wb = new XSSFWorkbook(fis);
    // Creates a Sheet object to retrieve the object
    XSSFSheet sheet = wb.getSheetAt(0);
    // List of recipients
    List<String> recipients = new ArrayList<String>();
    
    for (Row row : sheet) {
      // Retrieve cell in current row in whichever column was specified as containing phone numbers
      Cell cell = row.getCell(COLUMN_WITH_NUMS);
      // Set cell type to string so it can be read and edited easily
      cell.setCellType(Cell.CELL_TYPE_STRING);
      // Value of cell
      String value = cell.getStringCellValue();
      // Recipient number (always begins with +)
      String number = "+";
      // Runs through cell value and appends digits to the number to be returned, discarding
      // non-numbers (e.g. spaces, dashes)
      for (int i = 0; i < value.length(); i++) {
        Character c = value.charAt(i);
        if (Character.isDigit(c)) {
          number += c;
        }
      }
      // Adds cell value to list of recipients if it contains a number (discards those without,
      // e.g. headers, blank cells)
      if (number != "+") {
        recipients.add(number);
      }
    }
    // Returns list of recipient numbers
    return recipients;
  }
}
