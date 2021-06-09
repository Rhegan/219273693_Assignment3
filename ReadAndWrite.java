/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.assignment3;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Rhegan Albert Fortuin (219273693)
 */
public class ReadAndWrite {
    ObjectInputStream input;
    FileWriter writer;
    BufferedWriter buffWriter;
    
    Object object;
    Customer customer;
    Supplier supplier;
    
    String cust = "Customer";
    String sup = "Supplier";
    
    private ArrayList<Customer> customerArray = new ArrayList<Customer>();
    private ArrayList<Supplier> supplierArray = new ArrayList<Supplier>();
    
    //Question 2a
    public void openFile(){
        try
        {
            input = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
            System.out.println("File successfully opened");
        }
        
        catch(IOException ioe)
        {
            System.out.println("Error opening file..." + ioe.getMessage());
        }
    }
    
    public void readFromFile(){
        try
        {
            while(true){
                object = input.readObject();
                
                if(object.getClass() == Customer.class)
                {
                    customer = (Customer) object;
                    customerArray.add(customer);
                    
                }
                else if(object.getClass() == Supplier.class)
                {
                    supplier = (Supplier) object;
                    supplierArray.add(supplier);
                }
            }
        }
        catch(EOFException eofe)
        {
            System.out.println("EoF");
        }
        
        
        catch(IOException | ClassNotFoundException ioe)
        {
            System.out.println("Error reading from file..." + ioe.getMessage());
        }
        sortCustomer();
    }
    //Question 2b
    public void sortCustomer(){
        String[] sortID = new String[customerArray.size()];
        ArrayList<Customer> sortCust = new ArrayList<>();
        int customerArraySize = customerArray.size();
        
        for(int i = 0; i < customerArraySize; i++)
        {
            sortID[i] = customerArray.get(i).getStHolderId();
        }
        Arrays.sort(sortID);
        
        for(int i = 0; i < customerArraySize; i++)
        {
            for(int j = 0; j < customerArraySize; j++)
            {
                if(sortID[i].equals(customerArray.get(j).getStHolderId()))
                {
                    sortCust.add(customerArray.get(j));
                }
            }
        }
        customerArray.clear();
        customerArray = sortCust;
    }
    //Question 3a
    public void sortSupplier(){
        String[] sortID = new String[supplierArray.size()];
        ArrayList<Supplier> sortSup = new ArrayList<>();
        int supplierArraySize = supplierArray.size();
        
        for(int i = 0; i < supplierArraySize; i++)
        {
            sortID[i] = supplierArray.get(i).getStHolderId();
        }
        Arrays.sort(sortID);
        
        for(int i = 0; i < supplierArraySize; i++)
        {
            for(int j = 0; j < supplierArraySize; j++)
            {
                if(sortID[i].equals(supplierArray.get(j).getStHolderId()))
                {
                    sortSup.add(supplierArray.get(j));
                }
            }
        }
        supplierArray.clear();
        supplierArray = sortSup;
    }
    //Question 2c
    public int fetchAge(String DoB){
        String[] seperation = DoB.split("-");
        
        LocalDate birthDate = LocalDate.of(Integer.parseInt(seperation[0]), Integer.parseInt(seperation[1]), Integer.parseInt(seperation[2]));
        LocalDate currentAge = LocalDate.now();
        Period difference = Period.between(birthDate, currentAge);
        int age = difference.getYears();
        
        return age;
    }
    //Question 2d
    public String formatDob(Customer DoB){
        LocalDate DoBFormat = LocalDate.parse(DoB.getDateOfBirth());
        DateTimeFormatter changeDoBFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        
        return DoBFormat.format(changeDoBFormat);
    }
    //Question 2e
    public void showCustomersText(){
        try{
            writer = new FileWriter("customerOutFile.txt");
            buffWriter = new BufferedWriter(writer);
            buffWriter.write(String.format("%s\n","************************************Customer************************************"));
            
            buffWriter.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", "ID","Name","Surname","Date of Birth","Age"));
            buffWriter.write(String.format("%s\n","********************************************************************************"));
            
            for (int i = 0; i < customerArray.size(); i++)
            {
                buffWriter.write(String.format("%-15s %-15s %-15s %-15s %-15s \n", customerArray.get(i).getStHolderId(), customerArray.get(i).getFirstName(), customerArray.get(i).getSurName(), formatDob(customerArray.get(i)),fetchAge(customerArray.get(i).getDateOfBirth())));
            }
            
            buffWriter.write(String.format("%s\n"," "));
            buffWriter.write(String.format("%s\n"," "));
            buffWriter.write(String.format("%s\n",rent()));
        }
        catch(IOException fnfe)
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try
        {
            buffWriter.close( ); 
        }
        catch (IOException ioe)
        {            
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    //Question 2f
    public String rent(){
        int count = customerArray.size();
        int rentAbility = 0;
        int rentUnability = 0;
        
        for(int i = 0; i < count; i++)
        {
            if(customerArray.get(i).getCanRent())
            {
                rentAbility++;
            }
            else
            {
                rentUnability++;
            }
        }
        String line = "Number of customers who can rent : "+ '\t' + rentAbility + '\n' + "Number of customers who cannot rent : "+ '\t' + rentUnability;
        return line;
    }
    //Question 3b
    public void showSupplierText(){
        try
        {
            writer = new FileWriter("supplierOutFile.txt");
            buffWriter = new BufferedWriter(writer);
            buffWriter.write(String.format("%s\n","************************************SUPPLIERS************************************"));
           
            buffWriter.write(String.format("%-15s %-15s \t %-15s %-15s \n", "ID","Name","Prod Type","Description"));
            buffWriter.write("*********************************************************************************\n");
            for (int i = 0; i < supplierArray.size(); i++)
            {
                buffWriter.write(String.format("%-15s %-15s \t %-15s %-15s \n", supplierArray.get(i).getStHolderId(), supplierArray.get(i).getName(), supplierArray.get(i).getProductType(),supplierArray.get(i).getProductDescription()));
            }
            
        }
        catch(IOException fnfe )
        {
            System.out.println(fnfe);
            System.exit(1);
        }
        try
        {
            buffWriter.close( ); 
        }
        catch (IOException ioe)
        {            
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    public void closeFile(){
        try
        {
            input.close();
            System.out.println("File successfully closed");
        }
        
        catch(IOException ioe)
        {
            System.out.println("Error closing file..." + ioe.getMessage());
            System.exit(1);
        }
    }
    
    public static void main(String[] args) {
        ReadAndWrite obj = new ReadAndWrite();
        obj.openFile();
        obj.readFromFile();
        obj.closeFile();;
        obj.showCustomersText();
        obj.showSupplierText();
    }
}
