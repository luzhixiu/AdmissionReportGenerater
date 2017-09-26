package csvdataanalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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

public class CsvDataAnalysis {

    static ArrayList<ECAP> ecapList = new ArrayList();

    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("2017FA"));
        String header = sc.nextLine();
        //Master Setting
        String gender = "F";
        String studentType = "PartTime";//Freshmen, Transfer,Readmission,PartTime

        ArrayList<Candidate> candidateList = new ArrayList();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String list[] = line.split(",", -1);
            if (list[0].matches("[0-9]+")) {
//                System.out.println(list.length);
                Candidate cd = new Candidate(list);
                //Master Filter
                if (studentType.contains("Freshmen")) {
                    if (cd.AdmissionStatus.contains("First") && cd.Gender.contains("")) {//Freshean
                        candidateList.add(cd);
                    }
                } else if (studentType.contains("Transfer")) {
                    if (cd.Gender.contains(gender) && cd.AdmissionStatus.contains("Transfer")) {//Transfer
                        candidateList.add(cd);
                    }
                } else if (studentType.contains("Readmission")) {
                    if (cd.Gender.contains(gender) && cd.AdmissionStatus.contains("Readmission")) {//Readmission
                        candidateList.add(cd);
                    }
                } else if (studentType.contains("PartTime")) {
                    if (cd.Gender.contains(gender) && !cd.Intent.contains("F")) {//PartTime or HalfTime
                        candidateList.add(cd);
                    }
                }
            }
        }
        System.out.println(candidateList.size());
        //ECAPConfig
        sc = new Scanner(new File("2017ECAP.csv"));

        header = sc.nextLine();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String list[] = line.split(",", -1);
            if (list[2].length() <= 1) {
                continue;
            }
            ECAP ecap = new ECAP(list);
            if (studentType.contains("Freshmen")) {
                if (ecap.Gender.contains(gender)) {
                    ecapList.add(ecap);
                }
            } else if (studentType.contains("Transfer")) {
                if (ecap.Gender.contains(gender)) {
                    if (ecap.MarketSegment.contains("Transfer")) {
                        ecapList.add(ecap);
                    } else if (ecap.Region.contains("INLR")) {
                        ecapList.add(ecap);
                    }
                }
            } else if (studentType.contains("Readmission")) {
                if (ecap.Gender.contains(gender)) {
                if (ecap.MarketSegment.contains("Re-admits")) {
                        ecapList.add(ecap);
                    }
                }
            } else if (studentType.contains("PartTime")) {// ecap does not involve  Part time students
                ecapList.clear();
            }

        }
        System.out.println(ecapList.size());
        System.out.println(reportTotalApplicants(candidateList, ecapList) + " Total Applicants");
        System.out.println(reportTotalAccept(candidateList) + " TotalAccept");
        System.out.println(reportTotalDeposits(candidateList) + " TotalDeposits");
        System.out.println(reportResidential(candidateList) + " Residential");
        System.out.println(reportCommute(candidateList) + " Commute");
        System.out.println(reportHousingContracts(candidateList) + " HousingContracts");
        System.out.println(reportExemptions(candidateList) + " Exemptions");
        System.out.println(reportIncomplete(candidateList) + " Incomplete");
        System.out.println(reportAccepted(candidateList) + " Accepted");
        System.out.println(reportDeposited(candidateList) + " Deposited");
        System.out.println(reportTotalActive(candidateList) + " TotalActive");
        System.out.println(reportCancelledApplicants(candidateList) + " CancelledApplicants");
        System.out.println(reportDeniedApplicants(candidateList) + " DeniedApplicants");
        System.out.println(reportWithDrawnApplicants(candidateList) + " WithDrawnApplicants");
        System.out.println(reportDepositedWDR(candidateList) + " DepositedWDR");
        System.out.println(reportTotalCancelled(candidateList) + " TotalCancelled");

    }

    static public int reportTotalApplicants(ArrayList<Candidate> list, ArrayList<ECAP> ecapList) {
        int cnt = 0;
        cnt += list.size();
        cnt += ecapList.size();
        return cnt;
    }

    static public void printCandidate(Candidate cd) {
        System.out.print("Attribute number " + cd.list.length + " ");
        for (int i = 0; i < cd.list.length; i++) {
            System.out.print(cd.list[i] + " ");

        }
        System.out.println("");
    }

    static public int reportTotalAccept(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (!cd.CurrentApplicationStatus.equals("Pending")) {
                if (!cd.CurrentApplicationStatus.equals("Applied")) {
                    if (!cd.CurrentApplicationStatus.equals("Canceled")) {
                        if (!cd.CurrentApplicationStatus.equals("Denied")) {

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
            if (cd.PrimaryState.equals("IA")) {
                cnt++;
            }
        }
        return cnt;
    }

    static int reportTotalDeposits(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.DepositDate.length() >= 1) {

                cnt++;

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

    static int reportIncomplete(ArrayList<Candidate> list) {
        int cnt = reportTotalApplicants(list, ecapList) - reportAccepted(list) - reportDeposited(list) - reportTotalCancelled(list);
        return cnt;
    }
//    

    static int reportAccepted(ArrayList<Candidate> list) {
        int cnt = reportTotalAccept(list) - reportWithDrawnApplicants(list) - reportDepositedWDR(list) - reportDeposited(list);
        return cnt;
    }

    static int reportDeposited(ArrayList<Candidate> list) {
        int cnt = reportTotalDeposits(list) - reportDepositedWDR(list);
        return cnt;
    }

    static int reportTotalActive(ArrayList<Candidate> list) {
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
        for (ECAP candidate : ecapList) {
            if (candidate.CancelDate.length() >= 1) {
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
                if (cd.DepositDate.length() == 0) {
                    cnt++;
                }
            }
        }
        return cnt;

    }

    static int reportDepositedWDR(ArrayList<Candidate> list) {
        int cnt = 0;
        for (Candidate cd : list) {
            if (cd.CurrentApplicationStatus.contains("Withdrawn")) {
                if (cd.DepositDate.length() > 0) {
                    cnt++;
                }
            }
        }
        return cnt;

    }

    static int reportTotalCancelled(ArrayList<Candidate> list) {
        int cnt = reportCancelledApplicants(list) + reportDeniedApplicants(list) + reportWithDrawnApplicants(list) + reportDepositedWDR(list);
        return cnt;

    }
}
