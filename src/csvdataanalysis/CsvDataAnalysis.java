package csvdataanalysis;

import static csvdataanalysis.checkDate.year;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

class Candidate {

    String StudentID, FirstName, LastName, PrimaryCity, PrimaryState, AdmissionStatus, Region;
    String MarketSegMent, Application_Record_Name, Program, Gender, Intent, CurrentApplicationStatus;
    String ApplicationDate, AdmissionDecisionDate, DepositDate, ApplicationCanceWithdrawDate;
    String CommuterResident, HousingAppOrExemptionStatus, FastTrackDate, Trans_REA_ADL_INTL_RegistrationDate;
    String UG_RegisteredCredit, Enrolled_Major;
    String[] list;

    public Candidate(String[] header) {
        list = new String[0];
        StudentID = header[0];
        FirstName = header[1];
        LastName = header[2];
        PrimaryCity = header[3];
        PrimaryState = header[4];
        AdmissionStatus = header[5];
        Region = header[6];
        MarketSegMent = header[7];
        Application_Record_Name = header[8];
        Program = header[9];
        Gender = header[10];
        Intent = header[11];
        CurrentApplicationStatus = header[12];
        ApplicationDate = header[13];
        AdmissionDecisionDate = header[14];
        DepositDate = header[15];
        ApplicationCanceWithdrawDate = header[16];
        CommuterResident = header[17];
        HousingAppOrExemptionStatus = header[18];
        FastTrackDate = header[19];
        Trans_REA_ADL_INTL_RegistrationDate = header[20];
        UG_RegisteredCredit = header[21];
        Enrolled_Major = header[22];
        list = header.clone();
    }

}

class ECAP {

    String firstName, lastName, Region, CancelDate, Department, GraduateYear, Gender, MarketSegment, PrimaryState;
    String[] list = new String[0];

    public ECAP(String[] header) {
        firstName = header[0];
        lastName = header[1];
        Region = header[2];
        CancelDate = header[3];
        Department = header[4];
        GraduateYear = header[5];
        Gender = header[6];
        MarketSegment = header[7];
        PrimaryState = header[8];
        list = header.clone();
    }

}

public class CsvDataAnalysis extends JComponent implements Accessible {
    static ArrayList<Candidate> FAList = new ArrayList();
    static ArrayList<ECAP> ECAPList = new ArrayList();
    //Master Setting
    static String gender = "F";
    static String studentType = "Freshmen";//Freshmen, Transfer,Readmission,PartTime
    static int weekNumber=0;
    static int year=2017;
    static File curDir;
    static String[][] Matrix = new String[35][35];

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File FAFile,ECAPFile;
        
//        File FAFile = new File("C:\\Users\\zhixiu.lu\\Desktop\\2017FA_Weekly.csv");
        FAFile=loadFaFile();
        ECAPFile=loadEcapFile();
        
        System.out.println("Selected file: " + FAFile.getAbsolutePath());
        System.out.println("Selected file: " + ECAPFile.getAbsolutePath());
        year=Integer.parseInt(JOptionPane.showInputDialog("Please Enter the Entry Year of This Report"));
        weekNumber = Integer.parseInt(JOptionPane.showInputDialog("Please Enter the Week Number of This Report"));
       

        gender = "F";
        studentType = "Freshmen";
        loadFAList(FAFile);
//        System.out.println(FAList.size()+" size of FAList");
        loadECAPList(ECAPFile);
//        System.out.println(ECAPList.size()+" size of ECAPList");
        fillInMatrix(1);
      

        gender = "M";
        studentType = "Freshmen";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(2);

        gender = "";
        studentType = "Freshmen";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(3);

        gender = "F";
        studentType = "Transfer";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(4);

        gender = "M";
        studentType = "Transfer";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(5);

        gender = "";
        studentType = "Transfer";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(6);

        gender = "F";
        studentType = "Readmission";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(7);

        gender = "M";
        studentType = "Readmission";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(8);

        gender = "";
        studentType = "Readmission";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(9);

        gender = "F";
        studentType = "PartTime";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(10);

        gender = "M";
        studentType = "PartTime";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(11);

        gender = "";
        studentType = "PartTime";
        loadFAList(FAFile);
        loadECAPList(ECAPFile);
        fillInMatrix(12);
        makeSum();
        fillDashboardTitles();
        writeToFile();
        showMessage("Program Finished","");

    }

    static public void showMessage(String message,String titleBar){
JOptionPane.showMessageDialog(null, message, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    
    
    }
    
    
    
    
    static public File loadFaFile() {
        JFileChooser FAFileChooser = new JFileChooser();
        FAFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FAFileChooser.setDialogTitle("Please choose the FA.csv file");
        int result = FAFileChooser.showOpenDialog(null);
        File FA = new File("");

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = FAFileChooser.getSelectedFile();
            curDir=FAFileChooser.getCurrentDirectory();
            FA = selectedFile;
        }
        return FA;
    }

    static public File loadEcapFile() {

        JFileChooser ECAPFileChooser = new JFileChooser();
        ECAPFileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        ECAPFileChooser.setDialogTitle("Please choose the ECAP.csv file");
        int ECAPresult = ECAPFileChooser.showOpenDialog(null);
        File ECAPFile = new File("");
        if (ECAPresult == JFileChooser.APPROVE_OPTION) {
            File selectedFile = ECAPFileChooser.getSelectedFile();
            ECAPFile = selectedFile;
            System.out.println("Selected file: " + ECAPFile.getAbsolutePath());
        }
        return ECAPFile;
    }

    static public void loadFAList(File FA) throws FileNotFoundException {
        FAList.clear();
        Scanner sc = new Scanner(FA);
        String header = sc.nextLine();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String list[] = line.split(",", -1);
            if (list.length <= 1) {
                continue;
            }
            if (containsOnlyIDs(list[0])) {
//                System.out.println(list.length);
                Candidate cd = new Candidate(list);
                //Master Filter
                if (studentType.contains("Freshmen")) {
                    if (cd.Gender.contains(gender) && cd.AdmissionStatus.contains("First")) {//Freshean
                        FAList.add(cd);
                    }
                } else if (studentType.contains("Transfer")) {
                    if (cd.Gender.contains(gender) && cd.AdmissionStatus.contains("Transfer")) {//Transfer
                        FAList.add(cd);
                    }
                } else if (studentType.contains("Readmission")) {
                    if (cd.Gender.contains(gender) && cd.AdmissionStatus.contains("Readmission")) {//Readmission
                        FAList.add(cd);
                    }
                } else if (studentType.contains("PartTime")) {
                    if (cd.Gender.contains(gender) && !cd.Intent.contains("F")) {//PartTime or HalfTime
                        FAList.add(cd);
                    }
                }
            }
        }
    }

    static public void loadECAPList(File ECAPFile) throws FileNotFoundException {
        ECAPList.clear();
        Scanner sc = new Scanner(ECAPFile);
        String header = sc.nextLine();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String list[] = line.split(",", -1);
            if (list.length <= 2) {
                continue;
            }

            ECAP ecap = new ECAP(list);
            if (studentType.contains("Freshmen")) {
                if (ecap.Gender.contains(gender)) {
                    ECAPList.add(ecap);
                }
            } else if (studentType.contains("Transfer")) {
                if (ecap.Gender.contains(gender)) {
                    if (ecap.MarketSegment.contains("Transfer")) {
                        ECAPList.add(ecap);
                    } else if (ecap.Region.contains("INLR")) {
                        ECAPList.add(ecap);
                    }
                }
            } else if (studentType.contains("Readmission")) {
                if (ecap.Gender.contains(gender)) {
                    if (ecap.MarketSegment.contains("Re-admits")) {
                        ECAPList.add(ecap);
                    }
                }
            } else if (studentType.contains("PartTime")) {// ecap does not involve  Part time students
                ECAPList.clear();
            }

        }

    }
    //check that the Date given(int string format) is before report date(figured out by week number)
    // String s is in the format "MM/dd/yyyy" , need to rid the quotation mark
    static public boolean checkDate(String s) throws FileNotFoundException {
        s=s.substring(1,s.length()-1);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        Date validDate=weekNumberToDate();
        try {
            date = df.parse(s);
            if (date.compareTo(validDate)<0)return true;
            else return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    static public Date weekNumberToDate() throws FileNotFoundException {
        String StartDate="";
        Map<String,String> map=new HashMap();
        Scanner sc=new Scanner(new File( (System.getProperty("user.dir")+"/EntryYearStartDateConfig.txt")));
        while(sc.hasNext()){
            String line=sc.nextLine();

            if (line.length()>4)map.put(line.split(",")[0].trim(),line.split(",")[1].trim());
        }

        StartDate=map.get(year+"");
        Date dt=new Date(StartDate);
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        for (int i=0;i<weekNumber;i++){
            c.add(Calendar.DATE, 7);
        }
        dt = c.getTime();
        return dt;
        

    }
    
    
    
    
    

    static public int reportTotalApplicants(ArrayList<Candidate> list, ArrayList<ECAP> ecapList) {
        int cnt = 0;
        cnt += list.size();
//        System.out.println("FA List Size: "+list.size());
        cnt += ecapList.size();
//        System.out.println("ECAPList Size"+ ecapList.size());
        return cnt;
    }

    static public void printCandidate(Candidate cd) {
        System.out.print("Attribute number " + cd.list.length + " ");
        for (String list : cd.list) {
            System.out.print(list + " ");
        }
        System.out.println("");
    }

    static public int reportTotalAccept(ArrayList<Candidate> list) {
        int cnt = 0;
        System.out.println(list.size()+"-----");
        for (Candidate cd : list) {
            if (!cd.CurrentApplicationStatus.contains("Pending")) {
                if (!cd.CurrentApplicationStatus.contains("Applied")) {
                    if (!cd.CurrentApplicationStatus.contains("Canceled")) {
                        if (!cd.CurrentApplicationStatus.contains("Denied")) {
                            System.out.println(cd.CurrentApplicationStatus);
                            cnt++;

                        }
                    }
                }

            }

        }
        return cnt;
    }

    static int reportState(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.PrimaryState.contains("IA")) {
                cnt++;
            }
        }
        return cnt;
    }

    static int reportTotalDeposits(ArrayList<Candidate> list) throws FileNotFoundException {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.DepositDate.length() >= 3) {
                if (checkDate(cd.DepositDate)) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    static int reportResidential(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.CommuterResident.contains("Resident")) {
                cnt++;

            }
        }
        return cnt;
    }

    static int reportCommute(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.CommuterResident.contains("Commuter")) {

                cnt++;

            }
        }
        return cnt;
    }

    static int reportHousingContracts(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.HousingAppOrExemptionStatus.contains("Room") || cd.HousingAppOrExemptionStatus.contains("Housing")) {

                cnt++;

            }
        }
        return cnt;
    }

    static int reportExemptions(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.HousingAppOrExemptionStatus.contains("Exemption Approved")) {

                cnt++;

            }
        }
        return cnt;
    }

    static int reportIncomplete(ArrayList<Candidate> list) throws FileNotFoundException {
        int cnt = reportTotalApplicants(list, ECAPList) - reportAccepted(list) - reportDeposited(list) - reportTotalCancelled(list);
        if (cnt<0)return 0;
        return cnt;
    }
//    

    static int reportAccepted(ArrayList<Candidate> list) throws FileNotFoundException {
        int cnt = reportTotalAccept(list) - reportWithDrawnApplicants(list) - reportDepositedWDR(list) - reportDeposited(list);
        return cnt;
    }

    static int reportDeposited(ArrayList<Candidate> list) throws FileNotFoundException {
        int cnt = reportTotalDeposits(list) - reportDepositedWDR(list);
        return cnt;
    }

    static int reportTotalActive(ArrayList<Candidate> list) throws FileNotFoundException {
        int cnt = reportAccepted(list) + reportIncomplete(list) + reportDeposited(list);
        return cnt;
    }

    static int reportCancelledApplicants(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.CurrentApplicationStatus.contains("Canceled")) {
                cnt++;
            }

        }
        for (ECAP candidate : ECAPList) {
            if (candidate.CancelDate.length() >= 3) {
                cnt++;
            }

        }
        return cnt;

    }

    static int reportDeniedApplicants(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.CurrentApplicationStatus.contains("Denied")) {
                cnt++;
            }
        }
        return cnt;

    }

    static int reportWithDrawnApplicants(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.CurrentApplicationStatus.contains("Withdrawn")) {
                if (cd.DepositDate.length() <= 3) {
                    cnt++;
                }
            }
        }
        return cnt;

    }

    static int reportDepositedWDR(ArrayList<Candidate> list) throws FileNotFoundException {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.CurrentApplicationStatus.contains("Withdrawn")) {
                if (cd.DepositDate.length() > 3) {
                    if (checkDate(cd.DepositDate))cnt++;
                }
            }
        }
        return cnt;

    }

    static int reportTotalCancelled(ArrayList<Candidate> list) throws FileNotFoundException {
        int cnt = reportCancelledApplicants(list) + reportDeniedApplicants(list) + reportWithDrawnApplicants(list) + reportDepositedWDR(list);
        return cnt;

    }

    static void displayDashBoard() throws FileNotFoundException {
        System.out.println(reportTotalApplicants(FAList, ECAPList) + " Total Applicants");
        System.out.println(reportTotalAccept(FAList) + " TotalAccept");
        System.out.println(reportTotalDeposits(FAList) + " TotalDeposits");
        System.out.println(reportResidential(FAList) + " Residential");
        System.out.println(reportCommute(FAList) + " Commute");
        System.out.println(reportHousingContracts(FAList) + " HousingContracts");
        System.out.println(reportExemptions(FAList) + " Exemptions");
        System.out.println(reportIncomplete(FAList) + " Incomplete");
        System.out.println(reportAccepted(FAList) + " Accepted");
        System.out.println(reportDeposited(FAList) + " Deposited");
        System.out.println(reportTotalActive(FAList) + " TotalActive");
        System.out.println(reportCancelledApplicants(FAList) + " CancelledApplicants");
        System.out.println(reportDeniedApplicants(FAList) + " DeniedApplicants");
        System.out.println(reportWithDrawnApplicants(FAList) + " WithDrawnApplicants");
        System.out.println(reportDepositedWDR(FAList) + " DepositedWDR");
        System.out.println(reportTotalCancelled(FAList) + " TotalCancelled");
    }

    static public void fillInMatrix(int i) throws FileNotFoundException {
        Matrix[i][4] = reportTotalApplicants(FAList, ECAPList) + "";
        Matrix[i][5] = reportTotalAccept(FAList) + "";
        Matrix[i][6] = reportTotalDeposits(FAList) + "";
        Matrix[i][7] = reportResidential(FAList) + "";
        Matrix[i][8] = reportCommute(FAList) + "";
        Matrix[i][9] = reportHousingContracts(FAList) + "";
        Matrix[i][10] = reportExemptions(FAList) + "";
        Matrix[i][16] = reportIncomplete(FAList) + "";
        Matrix[i][17] = reportAccepted(FAList) + "";
        Matrix[i][18] = reportDeposited(FAList) + "";
        Matrix[i][19] = reportTotalActive(FAList) + "";
        Matrix[i][24] = reportCancelledApplicants(FAList) + "";
        Matrix[i][25] = reportDeniedApplicants(FAList) + "";
        Matrix[i][26] = reportWithDrawnApplicants(FAList) + "";
        Matrix[i][27] = reportDepositedWDR(FAList) + "";
        Matrix[i][28] = reportTotalCancelled(FAList) + "";

    }

    //index of 2017 Total for Female total is 1,7,13,19. index of 2016 Total for Female total is 4,10,16,22
    //index of male is femal+1, total is female+2
    public static void makeSum() {
        Matrix[25][4] = "****";
        String[] totalList = new String[4];
        //2017 Female
        for (int i = 0; i < 33; i++) {
            totalList[0] = ridNull(Matrix[1][i]);
            totalList[1] = ridNull(Matrix[4][i]);
            totalList[2] = ridNull(Matrix[7][i]);
            totalList[3] = ridNull(Matrix[10][i]);
            int sum = sum(totalList);
            if (sum >= 0) {
                Matrix[13][i] = sum + "";
            }
        }

        //2017 Male
        for (int i = 0; i < 33; i++) {
            totalList[0] = ridNull(Matrix[2][i]);
            totalList[1] = ridNull(Matrix[5][i]);
            totalList[2] = ridNull(Matrix[8][i]);
            totalList[3] = ridNull(Matrix[11][i]);
            int sum = sum(totalList);
            if (sum >= 0) {
                Matrix[14][i] = sum + "";
            }
        }

        //2017 Total
        for (int i = 0; i < 33; i++) {
            totalList[0] = ridNull(Matrix[3][i]);
            totalList[1] = ridNull(Matrix[6][i]);
            totalList[2] = ridNull(Matrix[9][i]);
            totalList[3] = ridNull(Matrix[12][i]);
            int sum = sum(totalList);
            if (sum >= 0) {
                Matrix[15][i] = sum + "";
            }
        }


    }

    public static String ridNull(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    //In: A list of Strings which should only contain digits (Integer String)  
    //Out: return the sum of all the interger strings, if any of the string contains letters, return -1
    public static int sum(String[] list) {
        int sum = 0;
        for (String s : list) {
            if (s.isEmpty()) {
                return -1;
            }
            if (s.matches("[0-9]*")) {
                sum += Integer.parseInt(s);
            } else {
                return -1;
            }
        }
        return sum;
    }

    public static void fillDashboardTitles() throws FileNotFoundException {
        Scanner sc = new Scanner((new File( System.getProperty("user.dir")+"/location.txt")));
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.length() >= 3) {
                String list[] = line.split(",");
                int xCor = Integer.parseInt(list[1].trim());
                int yCor = Integer.parseInt(list[2].trim());
                
                if (list[0].charAt(0)=='_'){
                    Matrix[xCor][yCor-1]=year+list[0];
                
                }
                
                else Matrix[xCor][yCor - 1] = list[0];
                
            }

        }
        for (int i = 0; i < 33; i++) {
            for (int j = 0; j < 31; j++) {
                if (Matrix[j][i] == null) {
                    Matrix[j][i] = "";
                }
            }
        }
    }

    public static void printMatrix() {
        for (int i = 0; i < 33; i++) {
            for (int j = 0; j < 31; j++) {
                System.out.print(Matrix[j][i] + ",");
            }
            System.out.println("");
        }

    }

    public static void writeToFile() throws IOException {
        String name= year+"Week"+weekNumber+" DashBoard Report";
        Path filePath = Paths.get(curDir.toString(), (name+".csv"));
//        BufferedWriter writer = new BufferedWriter(new FileWriter(name + ".csv"));
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString()));
        String outputString=matrixToString();
        writer.write(outputString);
        writer.close();
    }

    public static String matrixToString() {
        String s = "";
        for (int i = 0; i < 33; i++) {
            String line = "";
            for (int j = 0; j < 31; j++) {
                line += (Matrix[j][i] + ",");
            }
            if (line.matches(".*[a-z].*")) {
                s += line + "\n";
            }
        }
        return s;
    }
    
    public static boolean containsOnlyIDs(String str) {
        if (str.length()>10)return false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
        }
        return false;
  }
    
    

}
