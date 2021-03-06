import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.Stacking;
import weka.classifiers.meta.Vote;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

// This file will be used to ensemble based prediction using stacking of algorithms
public class cross_log_pred_all_features
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
//String target_project = "cloudstack";
String target_project="hd";

//String source_project="cloudstack";
//String target_project = "tomcat";
//String target_project="hd";

//String source_project="hd";
//String target_project = "tomcat";
//String target_project="cloudstack";

String db_name ="logging6_crossif";
String result_table = "cross_pred_all_feature_"+type;

String source_file_path = path+"L6-CROSS-IF\\dataset\\"+source_project+"-arff\\"+type+"\\"+source_project+"_"+type+"_all_features.arff";		
String target_file_path = path+"L6-CROSS-IF\\dataset\\"+target_project+"-arff\\"+type+"\\"+target_project+"_"+type+"_all_features.arff";

DataSource trainsource;
DataSource testsource;
Instances trains;
Instances tests;
Evaluation result;

int instance_count_source = 0;
int instance_count_target =0;
Connection conn=null;	
java.sql.Statement stmt = null;


//This function uses dataset from the ARFF files
public void read_file()
{ 
try 
	{
	
		trainsource = new DataSource(source_file_path);
		trains = trainsource.getDataSet();
		trains.setClassIndex(0);
		
		testsource = new DataSource(target_file_path);
		tests = testsource.getDataSet();
		
		tests.setClassIndex(0);
		
		instance_count_source = trains.numInstances();
		instance_count_target = tests.numInstances();
		
		//System.out.println("Instance count source ="+ instance_count_source + "  Instance count target="+ instance_count_target);
   
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
	 
	  
	  //1. TF-IDF
	  StringToWordVector tfidf_filter = new StringToWordVector();
	  tfidf_filter.setIDFTransform(true);
	  tfidf_filter.setInputFormat(trains);
	  trains = Filter.useFilter(trains, tfidf_filter);     	  
	
	  tests = Filter.useFilter(tests, tfidf_filter);
 
     /*

     //2. Standarize  (not normalize because normalization is affected by outliers very easily)   	  
	  Standardize  std_filter =  new Standardize();
	  std_filter.setInputFormat(trains);
	  trains= Filter.useFilter(trains,std_filter);     	  
	 
	  tests= Filter.useFilter(tests,std_filter);  	  
     

     //3. Discretizations
	  Discretize dfilter = new Discretize();
     dfilter.setInputFormat(trains);
     trains = Filter.useFilter(trains, dfilter);
     
     tests = Filter.useFilter(tests, dfilter);	      
     */


	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}


public Evaluation cross_pred_adtree() 
{
		
Evaluation evaluation = null;
ADTree  m1 =  new ADTree();

try
{
	
    m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);
	//System.out.println("h2");
} catch (Exception e) 
{

	e.printStackTrace();
}
return evaluation;

}

//This function is used to train and test a using a given classifier
public Evaluation cross_pred_decision_table() 
{
		
Evaluation evaluation = null;
DecisionTable  m1 =  new DecisionTable();

try
{
	
  m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);
	//System.out.println("h2");
} catch (Exception e) 
{

	e.printStackTrace();
}
return evaluation;

}

//This function is used to train and test a using a given classifier
public Evaluation cross_pred_j48() 
{
		
Evaluation evaluation = null;
J48  m1 =  new J48();

try
{
	
  m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);
	//System.out.println("h2");
} catch (Exception e) 
{

	e.printStackTrace();
}
return evaluation;

}


//This function is used to train and test a using a given classifier
public Evaluation cross_pred_logistic() 
{
		
Evaluation evaluation = null;
Logistic  m1 =  new Logistic();

try
{
	
  m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);
	//System.out.println("h2");
} catch (Exception e) 
{

	e.printStackTrace();
}
return evaluation;

}



//This function is used to train and test a using a given classifier
public Evaluation cross_pred_random_forest() 
{
	
	
Evaluation evaluation = null;
RandomForest  m1 =  new RandomForest();

try
{

		
    m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);

	//System.out.println("h2");

} catch (Exception e) 
{

	e.printStackTrace();
}

return evaluation;

}
	


//This function is used to train and test a using a given classifier
public Evaluation cross_pred_naive_bayes() 
{
		
Evaluation evaluation = null;
NaiveBayes  m1 =  new NaiveBayes();

try
{
	
m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);
	//System.out.println("h2");
} catch (Exception e) 
{

	e.printStackTrace();
}
return evaluation;

}


//This function is used to train and test a using a given classifier
public Evaluation cross_pred_bayes_net() 
{
		
Evaluation evaluation = null;
BayesNet  m1 =  new BayesNet();

try
{
	
m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);
	//System.out.println("h2");
} catch (Exception e) 
{

	e.printStackTrace();
}
return evaluation;

}

//This function is used to train and test a using a given classifier
public Evaluation cross_pred_adaboost() 
{
		
Evaluation evaluation = null;
AdaBoostM1  m1 =  new AdaBoostM1();

try
{
	
m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);
	//System.out.println("h2");
} catch (Exception e) 
{

	e.printStackTrace();
}
return evaluation;

}

//This function is used to train and test a using a given classifier
public Evaluation cross_pred_rbfnetwork() 
{
		
Evaluation evaluation = null;
RBFNetwork  m1 =  new RBFNetwork();

try
{
	
m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);
	//System.out.println("h2");
} catch (Exception e) 
{

	e.printStackTrace();
}
return evaluation;

}


//This function is used to train and test a using a given classifier
public Evaluation cross_pred_svm() 
{
		
Evaluation evaluation = null;
SMO  m1 =  new SMO();

try
{
	
m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	//System.out.println("h1");
	evaluation.evaluateModel(m1, tests);
	//System.out.println("h2");
} catch (Exception e) 
{

	e.printStackTrace();
}
return evaluation;

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


//This method computes the average value  and std. deviation and inserts them in a db
public void compute_avg_stdev_and_insert(String classifier_name, double[] precision, double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{

// computes following metrics:
	/*
	 * 1. Precision
	 * 2. Recall
	 * 3. Accuracy
	 * 4. F measure
	 * 5. ROC-AUC
	 * */

	double avg_precision = 0.0;
	double avg_recall = 0.0;
	double avg_accuracy = 0.0;
	double avg_fmeasure = 0.0;	
	double avg_roc_auc = 0.0;
	
	double std_precision = 0.0;
	double std_recall = 0.0;
	double std_accuracy = 0.0;
	double std_fmeasure = 0.0;	
	double std_roc_auc = 0.0;
	//double total_instances = 0.0;
	
	util6_met  ut = new util6_met();
	
	avg_precision   = ut.compute_mean(precision);
	avg_recall      = ut.compute_mean(recall);
	avg_fmeasure    = ut.compute_mean(fmeasure);
	avg_accuracy    = ut.compute_mean(accuracy);
	avg_roc_auc     = ut.compute_mean(roc_auc);
	
	std_precision   = ut.compute_stddev(precision);
	std_recall      = ut.compute_stddev(recall);
	std_fmeasure    = ut.compute_stddev(fmeasure);
	std_accuracy    = ut.compute_stddev(accuracy);
	std_roc_auc     = ut.compute_stddev(roc_auc);
	
		
  // System.out.println("model ="+classifier_name +"   Acc = "+ avg_accuracy + "  size="+ pred_10_db.size());
	
	String insert_str =  " insert into "+ result_table +"  values("+ "'"+ source_project+"','"+ target_project+"','"+ classifier_name+"',"+ trains.numInstances() + ","+ tests.numInstances()+","
	                       + iterations+","+trains.numAttributes() +","+avg_precision+","+ std_precision+","+ avg_recall+","+ std_recall+","+avg_fmeasure+","+std_fmeasure+","+ avg_accuracy 
	                       +","+std_accuracy+","+ avg_roc_auc+","+ std_roc_auc+" )";
	System.out.println("Inserting="+ insert_str);
	
	conn = initdb(db_name);
	if(conn==null)
	{
		System.out.println(" Databasse connection is null");
		
	}
	
	try 
	{
		stmt = conn.createStatement();
		stmt.executeUpdate(insert_str);
		stmt.close();
		conn.close();
	} catch (SQLException e) {
		
		e.printStackTrace();
	}

}



private void learn_and_insert_adtree(double[] precision,		double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  ADTree for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_adtree();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("ADTree", precision, recall, accuracy, fmeasure , roc_auc );	   
}


private void learn_and_insert_decision_table(double[] precision,		double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  Decision Table for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_decision_table();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("Decision Table", precision, recall, accuracy, fmeasure , roc_auc );	   
}




private void learn_and_insert_j48(double[] precision,	double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  J48  for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_j48();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("J48", precision, recall, accuracy, fmeasure , roc_auc );	   
}



private void learn_and_insert_logistic(double[] precision,	double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  logistic  for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_logistic();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("Logistic", precision, recall, accuracy, fmeasure , roc_auc );	   
}



private void learn_and_insert_random_forest(double[] precision,
		double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  Random Forest for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_random_forest();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("Random Forest", precision, recall, accuracy, fmeasure , roc_auc );	   
}



private void learn_and_insert_naive_bayes(double[] precision,	double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  naive bayes  for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_naive_bayes();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("Naive Bayes", precision, recall, accuracy, fmeasure , roc_auc );	   
}


private void learn_and_insert_bayes_net(double[] precision,	double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  bayes net  for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_bayes_net();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("Bayes Net", precision, recall, accuracy, fmeasure , roc_auc );	   
}


private void learn_and_insert_adaboost(double[] precision,	double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  Adaboost for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_adaboost();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("AdaBoost", precision, recall, accuracy, fmeasure , roc_auc );	   
}

private void learn_and_insert_rbfnetwork(double[] precision,	double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  rbfnetwork  for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_rbfnetwork();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("RBFNetwork", precision, recall, accuracy, fmeasure , roc_auc );	   
}



private void learn_and_insert_svm(double[] precision,	double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc) 
{
System.out.println("Computing  svm  for:"+ type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file();
			   
				pre_process_data();
				result = cross_pred_svm();				
				
				precision[i]         =   result.precision(1)*100;
				recall[i]            =   result.recall(1)*100;
				accuracy[i]          =   result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				fmeasure[i]          =   result.fMeasure(1)*100;
				roc_auc[i]           =   result.areaUnderROC(1)*100;		
			
				//@ Un comment to see the evalauation results
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		   compute_avg_stdev_and_insert("SVM", precision, recall, accuracy, fmeasure , roc_auc );	   
}



//This is the main function
public static void main(String args[])
{	  	

	  cross_log_pred_all_features clps =  new cross_log_pred_all_features();
	
	  double precision[]   = new double[clps.iterations];
	  double recall[]      = new double[clps.iterations];
	  double accuracy[]    = new double[clps.iterations];
	  double fmeasure[]    = new double[clps.iterations];	
	  double roc_auc[]     = new double[clps.iterations];
		
	  //clps.learn_and_insert_adtree(precision, recall, accuracy,fmeasure,roc_auc);
	 // clps.learn_and_insert_j48(precision, recall, accuracy,fmeasure,roc_auc);
	 // clps.learn_and_insert_logistic(precision, recall, accuracy,fmeasure,roc_auc);
	//  clps.learn_and_insert_random_forest(precision, recall, accuracy,fmeasure,roc_auc);
	 // clps.learn_and_insert_naive_bayes(precision, recall, accuracy,fmeasure,roc_auc);
	 // clps.learn_and_insert_bayes_net(precision, recall, accuracy,fmeasure,roc_auc);
	  //clps.learn_and_insert_adaboost(precision, recall, accuracy,fmeasure,roc_auc);
	  //clps.learn_and_insert_rbfnetwork(precision, recall, accuracy,fmeasure,roc_auc);
	  //clps.learn_and_insert_svm(precision, recall, accuracy,fmeasure,roc_auc);
	    
	  
	  
     }//main	
	
}// classs




