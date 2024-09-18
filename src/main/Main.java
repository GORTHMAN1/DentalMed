package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import model.*;
import service.*;

public class Main {
	
	public static void showTreatments(Map<Integer, Treatment> treatmentsMap) {
		treatmentsMap.values().forEach(System.out::println);
	}
	
	public static void showAvailableDates(Map<Integer, AvailableDate> availableDatesMap) {
		availableDatesMap.values().forEach(System.out::println);
	}
	
	public static Treatment getTreatmentById(int treatmentId) {
        Map<Integer, Treatment> treatmentsMap = TreatmentsService.getTreatmentOptions();
        return treatmentsMap.get(treatmentId);
    }
	
	public static AvailableDate getAvailableDateById(int availableDateId) {
        Map<Integer, AvailableDate> availableDatesMap = AvailableDatesService.getAvailableDates();
        return availableDatesMap.get(availableDateId);
    }
	
	public static void showAppointments(List<Appointment> appointmentsList) {
		appointmentsList.forEach(System.out::println);
	}
	
	public static void updateAppointments(List<Appointment> appointmentsList, int chosenAppointmentId) {
		appointmentsList.removeIf(appointment -> appointment.getIdAppointment() == chosenAppointmentId);
	}
	
	public static void updateAvailableDatesMap(Map<Integer, AvailableDate>availableDatesMap, int chosenDateId) {
		availableDatesMap.remove(chosenDateId);
	}

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		int loop = 1;
		
		while(loop == 1) {
			System.out.println("-= DentalMed =-");
			System.out.println();
			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("3. Exit");
			System.out.println();
			
			int choice = 0;
			
			do {
				System.out.println("Choose option: ");
				choice = scanner.nextInt();
				
			} while (choice < 1 || choice > 3);
			
			if (choice == 1) {
				
				int loop1 = 0;
				
				do {
				
					choice = 0;
									
					String username;
					String password;
					
					System.out.println();
					System.out.println("-= Login =-");
					System.out.println("Username: ");
					
					username = scanner.next();
					
					System.out.println("Password: ");
					
					password = scanner.next();
					
					User loggedInUser = LoginUser.loginUser(username, password);
					
					if (loggedInUser != null && loggedInUser.getId() != 1) {
			       
			            System.out.println("Login successful!");
			            System.out.println();
			            
			            User user = loggedInUser;
			            			            
			            Map<Integer, Treatment> treatmentsMap = TreatmentsService.getTreatmentOptions();
						
			            List<Appointment> newAppointmentsList = new ArrayList<>();
						
			            List<Appointment> historyOfAppointmentsList = new ArrayList<>();
						
						Map<Integer, AvailableDate> availableDatesMap = AvailableDatesService.getAvailableDates();
					            
			            // Main menu
			            						
						int loop3 = 1;
						
						while(loop3 == 1) {
							
							System.out.println("-= DentalMed =-");
							System.out.println();
							System.out.println("1. Treatments");
							System.out.println("2. Available Dates");
							System.out.println("3. Make an Appointment");
							System.out.println("4. Cancel Appointments");
							System.out.println("5. View Appointments");
							System.out.println("6. Appointment History");
							System.out.println("7. Exit");
							System.out.println();
							System.out.println("Enter your choice:");
							
							choice = scanner.nextInt();
						
													
							if(choice == 1) {
								
								System.out.println("-= Treatments =-");
								System.out.println();
								
								showTreatments(treatmentsMap);
							    
							    System.out.println("Press 1 to return to main page");
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) {
							    	continue;
							    }
							    								
							}
							
							else if(choice == 2) {
								
								System.out.println("-= Available Dates =-");
					            System.out.println();
					            
					            showAvailableDates(availableDatesMap);
																
								System.out.println("Press 1 to return to main page");
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) {
							    	continue;
							    }
							}
							
							else if(choice == 3) {
								
								int option = 0;
								
								while(option == 0) {
									
									System.out.println("-= Make An Appointment =-");
									System.out.println();
									
									showAvailableDates(availableDatesMap);
									
									System.out.println();
									
									showTreatments(treatmentsMap);
									
									System.out.println();
									
									System.out.println("Choose an available date: (id)");
									
									int chosenDateId = scanner.nextInt();
									
									System.out.println("Choose a Treatment: (id)");
									
									int chosenTreatmentId = scanner.nextInt();
									
									newAppointmentsList.add(AppointmentService.createAppointment(getTreatmentById(chosenTreatmentId), getAvailableDateById(chosenDateId), user.getId()));
									
									if(newAppointmentsList.isEmpty()) {
										System.out.println("Appointment failed! Try Again");
										System.out.println();
									}
									
									else {
										System.out.println("Appointment made succesfully!");
										updateAvailableDatesMap(availableDatesMap, chosenDateId);
										System.out.println();
										
										showAppointments(newAppointmentsList);
										
										System.out.println("Press 1 to return to main page");
								    	
								    	choice = scanner.nextInt();
								    	
								    	if(choice == 1) {
								    		option = 1;
								    	}
									}
									
									System.out.println("");
									System.out.println("");
							    	
								}
								
							}
							
							else if(choice == 4) {
								
								int option = 0;
								
								newAppointmentsList = AppointmentService.getAppointmentsList();
								
								while(option == 0) {
									
									System.out.println("-= Cancel An Appointment =-");
									System.out.println();
									
									if(!newAppointmentsList.isEmpty()) {
										showAppointments(newAppointmentsList);
									}
									
									else {
										System.out.println("No appointments yet!");
										
										System.out.println("Press 1 to return to main page");
									    
									    choice = scanner.nextInt();
									    
									    if(choice == 1) {
									    	option = 1;
									    }
									}
									
									System.out.println("Choose appointment: (id)");
									
									int chosenAppointmentId = scanner.nextInt();
									
									boolean success = AppointmentService.deleteAppointmentAndUpdateDate(newAppointmentsList, chosenAppointmentId);
									
									if(success == false) {
										
										System.out.println("Cancelation failed! Try Again");
										System.out.println();
																				
									} 
									
									else {
										
										System.out.println("Appointment cancelled succesfully!");
										
										
										updateAppointments(newAppointmentsList, chosenAppointmentId);
										
										showAppointments(newAppointmentsList);
										System.out.println();
										
									}
														
									System.out.println("Press 1 to return to main page");
								    
								    choice = scanner.nextInt();
								    
								    if(choice == 1) {
								    	option = 1;
								    }
								    
								    System.out.println("");
									System.out.println("");
									
								}
								
							}
							
							else if(choice == 5) {
								
								System.out.println("-= Appointments =-");
								
								newAppointmentsList = AppointmentService.getAppointmentsList();
								
								if(!newAppointmentsList.isEmpty()) {
									showAppointments(newAppointmentsList);
								}
								
								else {
									System.out.println("No appointments yet!");
								}
								
								System.out.println("Press 1 to return to main page");
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) {
							    	continue;
							    }
							}
							
							else if(choice == 6) {
								
								System.out.println("-= Appointment History =-");
								System.out.println();
								
								System.out.println("(An appointment appears in the history when it is completed)");
								System.out.println();
								
								historyOfAppointmentsList = AppointmentService.getAppointmentsHistory();
								
								showAppointments(historyOfAppointmentsList);
								
								System.out.println("Press 1 to return to main page");
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) {
							    	continue;
							    }
							}
							
							else if(choice == 7) {
								return;
							}
						}

			            // End of Main menu
						
			        } else if(loggedInUser.getId() == 1){
			            

			        	System.out.println("Login successful!");
			            System.out.println();
			            
			            User admin = loggedInUser;
			            
			            Map<Integer, Treatment> treatmentsMap = TreatmentsService.getTreatmentOptions();
			            
			            List<Appointment> adminAppointmentsList = new ArrayList<>();
						
						Map<Integer, AvailableDate> availableDatesMap = AvailableDatesService.getAvailableDates();
			            
			            // Defining the necessary maps and lists
			            
			            int loop4 = 0;
			            
			            while(loop4 == 0) {
			            	
			            	System.out.println("-= DentalMed =-");
							System.out.println();
							System.out.println("1. View Treatments");
							System.out.println("2. Add Treatments");
							System.out.println("3. Delete Treatments");
							System.out.println("4. View Available Dates");
							System.out.println("5. Add Available Date");
							System.out.println("6. Delete Available Date");
							System.out.println("7. View Appointments");
							System.out.println("8. Exit");
							System.out.println();
							System.out.println("Enter your choice:");
							
							choice = scanner.nextInt();
							
							if(choice == 1) {
								
								System.out.println("-= Treatments =-");
								System.out.println();
								
								treatmentsMap = TreatmentsService.getTreatmentOptions();
								showTreatments(treatmentsMap);
							    
							    System.out.println("Press 1 to return to main page");
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) {
							    	continue;
							    }
								
							}
													
							else if(choice == 2) {
								
								System.out.println("-= Add a Treatment =-");
								System.out.println();
								
								System.out.println("Available treatments:");
								System.out.println();
								
								treatmentsMap = TreatmentsService.getTreatmentOptions();
								showTreatments(treatmentsMap);
								
								System.out.println();
															    
							    System.out.println("Press 1 to add a treatment");
							    System.out.println("Press 2 to go back");
							    
							    System.out.println();
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) {
							    	
							    	String treatmentName;
							    	int treatmentPrice;
							    	
							    	System.out.println("Enter the treatment name: ");
							    	
							    	treatmentName = scanner.next();
							    	
							    	System.out.println("Enter the treatment price: ");
							    	
							    	treatmentPrice = scanner.nextInt();
							    	
							    	System.out.println();
							    	
							    	boolean success = TreatmentsService.addTreatment(treatmentName, treatmentPrice);
							    	
							    	if(success == false) {
										
										System.out.println("Failed to add treatment! Please try Again");
										System.out.println();
																				
									} 
									
									else {
										
										System.out.println("Treatment added succesfully!");
																				
										System.out.println();
										
									}
							    	
							    } else if (choice == 2) {
							    	continue;
							    }
							    								
							}
							
							else if(choice == 3) {
								
								System.out.println("-= Delete a Treatment =-");
								System.out.println();
								
								System.out.println("Available treatments:");
								System.out.println();
								
								treatmentsMap = TreatmentsService.getTreatmentOptions();
								showTreatments(treatmentsMap);
								
								System.out.println();
															    
							    System.out.println("Press 1 to delete a treatment");
							    System.out.println("Press 2 to go back");
							    
							    System.out.println();
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) { 
							    	
							    	System.out.println("Enter treatment: (id)");
							    	
							    	choice = scanner.nextInt();
							    	
							    	int treatment = choice;
							    	
							    	boolean success = TreatmentsService.deleteTreatment(treatment);
							    	
							    	if(success == false) {
										
										System.out.println("Failed to delete treatment! Please try Again");
										System.out.println();
																																								
									} 
									
									else {
										
										System.out.println("Treatment deleted succesfully!");
																				
										System.out.println();
										
									}
							    	
							    } else if(choice == 2) {
							    	
							    	return;
							    	
							    }
								System.out.println("Press 1 to continue");
								
								choice = scanner.nextInt();
								
								if (choice == 1) {
									continue;
								}
							}
							
							else if(choice == 4) {
								
								System.out.println("-= Available Dates =-");
					            System.out.println();
					            
					            availableDatesMap = AvailableDatesService.getAvailableDates();
					            showAvailableDates(availableDatesMap);
																
								System.out.println("Press 1 to return to main page");
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) {
							    	continue;
							    }
								
							}
							
							else if(choice == 5) {
								
								System.out.println("-= Add An Available Date =-");
					            System.out.println();
					            
					            System.out.println("Available Dates:");
					            System.out.println();
					            
					            availableDatesMap = AvailableDatesService.getAvailableDates();
					            showAvailableDates(availableDatesMap);
					            
					            System.out.println();
					            
					            System.out.println("Press 1 to delete a treatment");
							    System.out.println("Press 2 to go back");
							    
							    System.out.println();
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) {
							    	
							    	String date;
							    	String hour;
							    	
							    	System.out.println("Enter the date (YYYY-MM-DD): ");
							    	
							    	date = scanner.next();
							    	
							    	System.out.println("Enter the hour (HH:MM): ");
							    	
							    	hour = scanner.next();
							    	
							    	System.out.println();
							    	
							    	boolean success = AvailableDatesService.addAvailableDate(date, hour);
							    	
							    	if(success == false) {
										
										System.out.println("Failed to add date! Please try Again");
										System.out.println();
																				
									} 
									
									else {
										
										System.out.println("Date added succesfully!");
																				
										System.out.println();
										
									}
							    	
							    } else if (choice == 2) {
							    	continue;
							    }
								
								System.out.println("Press 1 to continue");
								
								choice = scanner.nextInt();
								
								if (choice == 1) {
									continue;
								}
							}

							else if(choice == 6) {
								
								System.out.println("-= Delete An Available Date =-");
								System.out.println();
								
								System.out.println("Available Dates:");
								System.out.println();
								
								availableDatesMap = AvailableDatesService.getAvailableDates();
					            showAvailableDates(availableDatesMap);
								
								System.out.println();
															    
							    System.out.println("Press 1 to delete a date");
							    System.out.println("Press 2 to go back");
							    
							    System.out.println();
							    
							    choice = scanner.nextInt();
							    
							    if(choice == 1) { 
							    	
							    	System.out.println("Enter date: (id)");
							    	
							    	choice = scanner.nextInt();
							    	
							    	int date = choice;
							    	
							    	boolean success = AvailableDatesService.deleteAvailableDate(date);
							    	
							    	if(success == false) {
										
										System.out.println("Failed to delete date! Please try Again");
										System.out.println();
																																								
									} 
									
									else {
										
										System.out.println("Date deleted succesfully!");
																				
										System.out.println();
										
									}
							    	
							    } else if(choice == 2) {
							    	
							    	continue;
							    	
							    }
								System.out.println("Press 1 to continue");
								
								choice = scanner.nextInt();
								
								if (choice == 1) {
									continue;
								}
																
							}

							else if(choice == 7) {
								
								System.out.println("-= View Appointments =-");
								System.out.println();
								
								adminAppointmentsList = AppointmentService.getPendingAppointments();
								showAppointments(adminAppointmentsList);
								
								System.out.println();
								
								System.out.println("Press 1 to mark an appointment as done");
							    System.out.println("Press 2 to go back");
							    
							    System.out.println();
							    
							    choice = scanner.nextInt();
							    
							    if (choice == 1) {
							    	
							    	System.out.println("Enter appointment: (id)");
							    	System.out.println();
							    	
							    	choice = scanner.nextInt();
							    	
							    	int id = choice;
							    	
							    	boolean success = AppointmentService.markAppointmentAsDone(id);
							    	
							    	System.out.println();
							    	
							    } else if (choice == 2) {
							    	continue;
							    }
																
								System.out.println("Press 1 to continue");
								
								choice = scanner.nextInt();
								
								if (choice == 1) {
									continue;
								}
							}
							

							else if(choice == 8) {
								return;								
							}
			            	
			            }
			              
			        	
			        } else {
			        	
			        	System.out.println("Login failed. Invalid username or password. Please try again. ");     
			            		        	
			        }
					
				} while (loop1 == 0);
				
				
			}
			
			else if (choice == 2) {
				
				int loop2 = 0;
				
				do {
				
					choice = 0;
									
					String username;
					String password;
					String passwordRepeat;
					
					System.out.println();
					System.out.println("-= Register =-");
					System.out.println("Username: ");
					
					username = scanner.next();
					
					System.out.println("Password: ");
					
					password = scanner.next();
					
					System.out.println("Confirm password: ");
					
					passwordRepeat = scanner.next();
					
					if(RegisterUser.registerUser(username, password, passwordRepeat) == true) {
						
						System.out.println("Press any key to continue.");
						
						String one = scanner.next();
						
						if(one.equals("1"))
							loop2 = 1;
						else
							loop2 = 1;
					}
					
				} while (loop2 == 0);
			}
			
			else if (choice == 3) {
				
				return;
			}
			
		}
	}

}
