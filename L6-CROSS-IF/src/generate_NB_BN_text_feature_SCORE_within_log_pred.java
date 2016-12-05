
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.meta.Stacking;
import weka.classifiers.meta.Vote;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Range;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.AddID;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

// This file will be used to ensemble based prediction using stacking of algorithms
public class generate_NB_BN_text_feature_SCORE_within_log_pred
{



/*
String path = "E:\\Sangeeta\\Research\\";
String user_name =  "sangeetal";
String password = "sangeetal";
String url = "jdbc:mysql://localhost:3307/";
String driver = "com.mysql.jdbc.Driver"; 
 
// */

///*
String path = "F:\\Research\\";
String user_name =  "root";
String password = "1234";
String url = "jdbc:mysql://localhost:3306/";
String driver = "com.mysql.jdbc.Driver";
//*/


//String type = "catch";
String type = "if";

int iterations=1;
String source_project="tomcat";
//String source_project = "cloudstack";
//String source_project="hd";


String db_name ="logging6_crossif";
String result_table = source_project+"_"+type+"_training6_nb_bn_score";  //score will be generated and updated in the target table using source table
String source_file_path = path+"L6-CROSS-IF\\dataset\\"+source_project+"-arff\\"+type+"\\"+source_project+"_"+type+"_text_features_with_id.arff";		

int if_ids[];

DataSource trainsource;
DataSource testsource;

Instances trains_1;
Instances tests_1;

Instances trains_2;
Instances tests_2;

Evaluation result;

int instance_count_source = 0;
int instance_count_target =0;
Connection conn=null;	
java.sql.Statement stmt = null;
 DataSource allsource;
Instances all_data;


//This function uses dataset from the ARFF files
public void read_file()
{ 
try 
	{
	
	    allsource = new DataSource(source_file_path);
		all_data= allsource.getDataSet();	
		
		all_data.randomize(new java.util.Random(1));
		
		//all_data.setClassIndex(0); //  new line
		
		int trainSize = (int) Math.round(all_data.numInstances() * 0.5);
		int testSize = all_data.numInstances() - trainSize;
		
		trains_1 = new Instances(all_data, 0, trainSize);
		tests_1 = new Instances(all_data, trainSize, testSize);		
		
		trains_2 = tests_1;
		tests_2 =  trains_1;		
		
		trains_1.setClassIndex(0);		
		tests_1.setClassIndex(0);
		
		trains_2.setClassIndex(0);		
		tests_2.setClassIndex(0);
				
		//instance_count_source = trains.numInstances();
		//instance_count_target = tests.numInstances();
		
		System.out.println("Instance count source ="+ instance_count_source + "  Instance count target="+ instance_count_target+  source_file_path);
   
	} catch (Exception e) 
	{
	
		e.printStackTrace();
	}	  
	
}


//This function is used to pre-process the dataset
public void pre_process_data()
{

 try
   {	  
	  //part 1
	  StringToWordVector tfidf_filter1 = new StringToWordVector();
	  tfidf_filter1.setIDFTransform(true);
	  tfidf_filter1.setInputFormat(trains_1);
	  trains_1 = Filter.useFilter(trains_1, tfidf_filter1);     	  
	  tests_1 = Filter.useFilter(tests_1, tfidf_filter1);
	 
	
	  // part 2
	  StringToWordVector tfidf_filter2 = new StringToWordVector();
	  tfidf_filter2.setIDFTransform(true);
	  tfidf_filter2.setInputFormat(trains_1);
	  tfidf_filter2.setInputFormat(trains_2);
	  trains_2 = Filter.useFilter(trains_2, tfidf_filter2);     	  
	  tests_2 = Filter.useFilter(tests_2, tfidf_filter2);
 


	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

//This function is used to train and test a using a given classifier
public void generate_nb_bn_score(Classifier m1, Classifier m2, String classifier_acro) 
{

    read_file();	
    pre_process_data();
	
	Evaluation evaluation_1 = null;
	Evaluation evaluation_2 = null;
	
	try 
	{
		
		  
		Remove rm1 = new Remove();
		rm1.setAttributeIndices("2");
		FilteredClassifier fc1 = new FilteredClassifier();
		fc1.setFilter(rm1);
		fc1.setClassifier(m1);
		m1.buildClassifier(trains_1);
        evaluation_1= new Evaluation(trains_1);
     	evaluation_1.evaluateModel(m1, tests_1);    	

		Remove rm2 = new Remove();
		rm2.setAttributeIndices("2");
		FilteredClassifier fc2 = new FilteredClassifier();
		fc2.setFilter(rm2);
		fc2.setClassifier(m2);
     	m2.buildClassifier(trains_2);
        evaluation_2= new Evaluation(trains_2);
     	evaluation_2.evaluateModel(m2, tests_2);
	   
	   
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	System.out.println("Classifier 1="+m1.getClass().getName());
	System.out.println("Classifier 2="+m2.getClass().getName());

	try
	{

		conn = initdb(db_name);
		if(conn==null)
			{
				System.out.println(" Databasse connection is null");
	
			} 
 
		else
		{
			try 
				{

				int j=0;
				for (j = 0; j < tests_1.numInstances(); j++) 
					{
    
						double score[] ;
						Instance curr  =  tests_1.instance(j);  
						double actual = curr.classValue();

 
						score= m1.distributionForInstance(curr);
	
						String update_score = "update "+ result_table +"  set "+ source_project+ "_to_"+source_project+"_"+classifier_acro+"_score=" +score[1] + " where if_id="+ tests_1.instance(j).value(1);
						
						if(tests_2.instance(j).value(1) ==2)
							
						{System.out.println("update="+ j+ "  ID="+ tests_1.instance(j).value(1) + "  score="+ score[1]);}
	
						java.sql.Statement stmt = conn.createStatement();
						stmt.executeUpdate(update_score);
		

					}//for
				
				System.out.println("J="+j);
				
				System.out.println("Now test 2 will start");
				
				j=0;
				for (j = 0; j < tests_2.numInstances(); j++) 
				{

					double score[] ;
					Instance curr  =  tests_2.instance(j);  
					double actual = curr.classValue();


					score= m2.distributionForInstance(curr);

					String update_score = "update "+ result_table +"  set "+ source_project+ "_to_"+source_project+"_"+ classifier_acro +"_score=" +score[1]+" where if_id="+ tests_2.instance(j).value(1);
					if(tests_2.instance(j).value(1) ==2)
					{
					System.out.println("update="+ j+ "  ID="+ tests_2.instance(j).value(1) + "  score="+ score[1]);}

					java.sql.Statement stmt = conn.createStatement();
					stmt.executeUpdate(update_score);
	

				}//for
	

				System.out.println("J="+j);
				stmt.close();
				conn.close();
		
				} catch (SQLException e) 
				{
		
					e.printStackTrace();
				}
	

           }// else


		} catch (Exception e) 
		{

			e.printStackTrace();
			}
		

}

public Connection initdb(String db_name)
{
try {
	      Class.forName(driver).newInstance();
	      conn = DriverManager.getConnection(url+db_name,user_name,password);
	      //System.out.println(" dbname="+ db_name+ "user name"+ userName+ " password="+ password);
	      if(conn==null)
	      {
	    	  System.out.println(" Database connection is null. Check it.");
	      }
	      
	 } catch (Exception e) 
	 {
	      e.printStackTrace();
	 }
	return conn;
}


//generate fids
private void get_if_ids()
{
	
	
	Connection conn1 = initdb(db_name);
	if(conn1==null)
	{
		System.out.println(" Databasse connection is null");
		
	}
	
	String row_count = "select count(*) from " + result_table;
	String get_str = "select if_id from "+  result_table;
	
	try 
	{
		java.sql.Statement stmt1 = conn1.createStatement();
		
		ResultSet rs = stmt1.executeQuery(row_count);
		while(rs.next())
		{
			int no_of_id = rs.getInt(1);
			if_ids  =   new int[no_of_id];
			
			
			System.out.println(" no of id="+no_of_id);
		}
				
		rs = stmt1.executeQuery(get_str);
		
	    int i=0;
		while(rs.next())
		{
			int id = rs.getInt(1);
			if_ids[i]  =  id;
			i++;
			
			System.out.println(" id="+id);
		}
		
		stmt1.close();
		conn1.close();
		
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	
}

//This is the main function
public static void main(String args[])
{	  	

	  generate_NB_BN_text_feature_SCORE_within_log_pred gnbs =  new generate_NB_BN_text_feature_SCORE_within_log_pred();
	  
	  //gnbs.get_if_ids();  
	 
	 // gnbs.generate_nb_bn_score(new NaiveBayes(), new NaiveBayes(), "nb");
	  gnbs.generate_nb_bn_score(new BayesNet(), new BayesNet(),"bn");
	    
     }//main	

}// classs




