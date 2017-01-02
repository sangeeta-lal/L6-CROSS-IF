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
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

// This file will be used to ensemble based prediction for cross project prediction
//Note:
// 1. It uses only NB score
// 2. It uses threshold 
// 3. It uses bagging
public class with_log_pred_nb_bn_score_thres_bag_learning
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

int iterations=10;
String source_project="tomcat";
//String source_project="cloudstack";
//String source_project="hd";

String db_name ="logging6_crossif";
String result_table = "result_within_pred_clif_nb_bn_learning_"+type;

String source_file_path = path+"L6-CROSS-IF\\dataset\\"+source_project+"-arff\\"+type+"\\"+source_project+"_to_"+ source_project+"_"+type+"_with_in_nb_bn_score.arff";		
//String target_file_path = path+"L6-CROSS-IF\\dataset\\"+target_project+"-arff\\"+type+"\\"+source_project+"_to_"+ target_project+"_"+type+"_cross_nb_bn_score.arff";

//DataSource trainsource;
DataSource testsource;
DataSource allsource;

double precision[][];
double recall[][];
double fmeasure[][];
double accuracy[][];
double roc_auc[][];
double ba[][];

double no_of_features[];


Instances trains;
Instances tests;
Instances all_data;
Evaluation result;

int instance_count_source = 0;
int instance_count_target =0;
Connection conn=null;	
java.sql.Statement stmt = null;


//This function uses dataset from the ARFF files
public void read_file(int random_number)
{ 
try 
	{
	
	    System.out.println(" source: "+  source_file_path); //+ "  target:"+ target_file_path);
		//trainsource = new DataSource(source_file_path);
		
		allsource = new DataSource(source_file_path);
		all_data= allsource.getDataSet();	
		
		all_data.randomize(new java.util.Random(random_number));
		
		all_data.setClassIndex(0); //  new line
		
		int trainSize = (int) Math.round(all_data.numInstances() * 0.8);
		int testSize = all_data.numInstances() - trainSize;
		
		trains = new Instances(all_data, 0, trainSize);
		tests = new Instances(all_data, trainSize, testSize);
		
		trains.setClassIndex(0);
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
	//  StringToWordVector tfidf_filter = new StringToWordVector();
	//  tfidf_filter.setIDFTransform(true);
	//  tfidf_filter.setInputFormat(trains);
	//  trains = Filter.useFilter(trains, tfidf_filter);     	  
	
	//  tests = Filter.useFilter(tests, tfidf_filter);
 
    // /*

     //2. Standarize  (not normalize because normalization is affected by outliers very easily)   	  
	//  Standardize  std_filter =  new Standardize();
	 // std_filter.setInputFormat(trains);
	 // trains= Filter.useFilter(trains,std_filter);     	  
	 
	  //tests= Filter.useFilter(tests,std_filter);  	  
   //  */
/*
     //3. Discretizations
	  Discretize dfilter = new Discretize();
     dfilter.setInputFormat(trains);
     trains = Filter.useFilter(trains, dfilter);
     
     tests = Filter.useFilter(tests, dfilter);	      
     */

	 
	// 4. Remove BN Score from the dataset
	//    Remove rm =  new Remove();
	//    rm.setAttributeIndices("20");
	//	rm.setInputFormat(trains);
	//	trains = Filter.useFilter(trains, rm);     	  
	//	
	//	tests = Filter.useFilter(tests, rm);

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}





//This function is used to train and test a using a given classifier
public void within_pred(Classifier m1, String ensemble_type, int itr) 
{
Evaluation evaluation = null;


int len=  m1.getClass().getName().toString().split("\\.").length;
String classifier_name =  m1.getClass().getName().toString().split("\\.")[len-1];

try
{		
    m1.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	
	
	int number_of_threshold= 0;
	for(double thres=0.0; thres<=1.0; thres=thres+0.01)
	 {
		double tp=0.0, fp=0.0, tn =0.0,fn=0.0;
		
		for (int j = 0; j < tests.numInstances(); j++) 
		 {
		     
			double score[] ;
			Instance curr  =  tests.instance(j);  
			double actual = curr.classValue();
		  
			score= m1.distributionForInstance(curr);
			
			
			 double predicted = 0;
		     if ( score[1] <= thres) 
		     {   predicted = 0;} 
		     else 
		     { 	 predicted = 1 ;}
			 
		     if (actual == 1) 
		       {
			      if (predicted == 1) 
			      { 			       tp++;			      } 
			      else
			      { 			       fn++; 			      }
			     }

			 else if (actual == 0)
			   {
			      if (predicted == 0) 
			      { 			       tn++; 			      } 
			      else 
			      { 			       fp++; 			      }
			     }//else if

			
		 }//  for test instances
		
		util6_met ut6 =  new util6_met();
		
		
		
		/*double precision    =   ut6.compute_precision(tp, fp, tn, fn);
		double recall       =   ut6.compute_recall(tp, fp, tn, fn);
		double accuracy     =   ut6.compute_accuracy(tp, fp, tn, fn);
		double fmeasure     =   ut6.compute_fmeasure(tp, fp, tn, fn);
		double roc_auc      =   0.0;// result.areaUnderROC(1)*100;	
		double ba           =   0.0;// write a function for this computation
		
		
		compute_avg_stdev_and_insert(classifier_name, thres, ensemble_type, precision, recall, accuracy, fmeasure, roc_auc, ba);*/
		
		precision[itr][number_of_threshold]=ut6.compute_precision(tp, fp, tn, fn);
		double temp = ut6.compute_precision(tp, fp, tn, fn);
	
		recall[itr][number_of_threshold]= ut6.compute_recall(tp, fp, tn, fn);
		fmeasure[itr][number_of_threshold]=ut6.compute_fmeasure(tp, fp, tn, fn);
		accuracy[itr][number_of_threshold]=ut6.compute_accuracy(tp, fp, tn, fn);
		
		//ebecuase i want roc auc, hence i am doing evaluation here again. although its not required 
		evaluation.evaluateModel(m1, tests);
		roc_auc[itr][number_of_threshold] = evaluation.areaUnderROC(1)*100;// call some method here if possible	
		ba[itr][number_of_threshold]=0.0;
		
		 


		no_of_features[itr] =  trains.numAttributes();

		
		number_of_threshold =  number_of_threshold+1;
	 }// for thres
	
	System.out.println("precision ["+itr+"]["+0+"]="+ precision[itr][0]);//+ "  temp="+temp+ " thres= "+ thres + " tp="+ tp+ "  fp"+ fp +" fn="+fn+" tn="+tn);
	

} catch (Exception e) 
{ 	e.printStackTrace();  }

}
	/*

//This function is used to train and test a using a given classifier
public void within_pred_bagging(Classifier mo, String ensemble_type) 
{
	
Evaluation evaluation = null;

Bagging model =  new Bagging();	 //bagging
model.setClassifier(mo);  //bagging
model.setNumIterations(20); //bagging

int len=  mo.getClass().getName().toString().split("\\.").length;
String classifier_name =  mo.getClass().getName().toString().split("\\.")[len-1];

try
{	
  model.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	
	for(double thres=0.0; thres<=1.0; thres=thres+0.01)
	 {
		double tp=0.0, fp=0.0, tn =0.0,fn=0.0;
		
		for (int j = 0; j < tests.numInstances(); j++) 
		 {
		     
			double score[] ;
			Instance curr  =  tests.instance(j);  
			double actual = curr.classValue();
		  
			score= model.distributionForInstance(curr);
			
			
			 double predicted = 0;
		     if ( score[1] <= thres) 
		     {   predicted = 0;} 
		     else 
		     { 	 predicted = 1 ;}
			 
		     if (actual == 1) 
		       {
			      if (predicted == 1) 
			      { 			       tp++;			      } 
			      else
			      { 			       fn++; 			      }
			     }

			 else if (actual == 0)
			   {
			      if (predicted == 0) 
			      { 			       tn++; 			      } 
			      else 
			      { 			       fp++; 			      }
			     }//else if

			
		 }//  for test instances
		
		util6_met ut6 =  new util6_met();
		
		double precision    =   ut6.compute_precision(tp, fp, tn, fn);
		double recall       =   ut6.compute_recall(tp, fp, tn, fn);
		double accuracy     =   ut6.compute_accuracy(tp, fp, tn, fn);
		double fmeasure     =   ut6.compute_fmeasure(tp, fp, tn, fn);
		double roc_auc      =   0.0;// result.areaUnderROC(1)*100;	
		double ba           =   0.0;// write a function for this computation
		
		
		compute_avg_stdev_and_insert(classifier_name, thres, ensemble_type, precision, recall, accuracy, fmeasure, roc_auc, ba);
		
	 }// for thres

} catch (Exception e) 
{ 	e.printStackTrace();  }

}*/
	

//This function is used to train and test a using a given classifier
public void within_pred_bagging(Classifier mo, String ensemble_type, int itr) 
{
Evaluation evaluation = null;

Bagging model =  new Bagging();	 //bagging
model.setClassifier(mo);  //bagging
model.setNumIterations(20); //bagging


int len=  mo.getClass().getName().toString().split("\\.").length;
String classifier_name =  mo.getClass().getName().toString().split("\\.")[len-1];

try
{		
    model.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	
	
	int number_of_threshold= 0;
	for(double thres=0.0; thres<=1.0; thres=thres+0.01)
	 {
		double tp=0.0, fp=0.0, tn =0.0,fn=0.0;
		
		for (int j = 0; j < tests.numInstances(); j++) 
		 {
		     
			double score[] ;
			Instance curr  =  tests.instance(j);  
			double actual = curr.classValue();
		  
			score= model.distributionForInstance(curr);
			
			
			 double predicted = 0;
		     if ( score[1] <= thres) 
		     {   predicted = 0;} 
		     else 
		     { 	 predicted = 1 ;}
			 
		     if (actual == 1) 
		       {
			      if (predicted == 1) 
			      { 			       tp++;			      } 
			      else
			      { 			       fn++; 			      }
			     }

			 else if (actual == 0)
			   {
			      if (predicted == 0) 
			      { 			       tn++; 			      } 
			      else 
			      { 			       fp++; 			      }
			     }//else if

			
		 }//  for test instances
		
		util6_met ut6 =  new util6_met();
		
		
		
		/*double precision    =   ut6.compute_precision(tp, fp, tn, fn);
		double recall       =   ut6.compute_recall(tp, fp, tn, fn);
		double accuracy     =   ut6.compute_accuracy(tp, fp, tn, fn);
		double fmeasure     =   ut6.compute_fmeasure(tp, fp, tn, fn);
		double roc_auc      =   0.0;// result.areaUnderROC(1)*100;	
		double ba           =   0.0;// write a function for this computation
		
		
		compute_avg_stdev_and_insert(classifier_name, thres, ensemble_type, precision, recall, accuracy, fmeasure, roc_auc, ba);*/
		
		precision[itr][number_of_threshold]=ut6.compute_precision(tp, fp, tn, fn);
		double temp = ut6.compute_precision(tp, fp, tn, fn);
	
		recall[itr][number_of_threshold]= ut6.compute_recall(tp, fp, tn, fn);
		fmeasure[itr][number_of_threshold]=ut6.compute_fmeasure(tp, fp, tn, fn);
		accuracy[itr][number_of_threshold]=ut6.compute_accuracy(tp, fp, tn, fn);
		
		//ebecuase i want roc auc, hence i am doing evaluation here again. although its not required 
		evaluation.evaluateModel(model, tests);
		roc_auc[itr][number_of_threshold] = evaluation.areaUnderROC(1)*100;// call some method here if possible	
		ba[itr][number_of_threshold]=0.0;
		
		 


		no_of_features[itr] =  trains.numAttributes();

		
		number_of_threshold =  number_of_threshold+1;
	 }// for thres
	
	System.out.println("precision ["+itr+"]["+0+"]="+ precision[itr][0]);//+ "  temp="+temp+ " thres= "+ thres + " tp="+ tp+ "  fp"+ fp +" fn="+fn+" tn="+tn);
	

} catch (Exception e) 
{ 	e.printStackTrace();  }

}

/*

//This function is used to train and test a using a given classifier
public void within_pred_boosting(Classifier mo, String ensemble_type) 
{
	
Evaluation evaluation = null;

AdaBoostM1 model =  new AdaBoostM1();	 //boosting
model.setClassifier(mo);  //bagging
model.setNumIterations(20); //bagging

int len=  mo.getClass().getName().toString().split("\\.").length;
String classifier_name =  mo.getClass().getName().toString().split("\\.")[len-1];

try
{	
  model.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	
	for(double thres=0.0; thres<=1.0; thres=thres+0.01)
	 {
		double tp=0.0, fp=0.0, tn =0.0,fn=0.0;
		
		for (int j = 0; j < tests.numInstances(); j++) 
		 {
		     
			double score[] ;
			Instance curr  =  tests.instance(j);  
			double actual = curr.classValue();
		  
			score= model.distributionForInstance(curr);
			
			
			 double predicted = 0;
		     if ( score[1] <= thres) 
		     {   predicted = 0;} 
		     else 
		     { 	 predicted = 1 ;}
			 
		     if (actual == 1) 
		       {
			      if (predicted == 1) 
			      { 			       tp++;			      } 
			      else
			      { 			       fn++; 			      }
			     }

			 else if (actual == 0)
			   {
			      if (predicted == 0) 
			      { 			       tn++; 			      } 
			      else 
			      { 			       fp++; 			      }
			     }//else if

			
		 }//  for test instances
		
		util6_met ut6 =  new util6_met();
		
		double precision    =   ut6.compute_precision(tp, fp, tn, fn);
		double recall       =   ut6.compute_recall(tp, fp, tn, fn);
		double accuracy     =   ut6.compute_accuracy(tp, fp, tn, fn);
		double fmeasure     =   ut6.compute_fmeasure(tp, fp, tn, fn);
		double roc_auc      =   0.0;// result.areaUnderROC(1)*100;	
		double ba           =   0.0;// write a function for this computation
		
		
		compute_avg_stdev_and_insert(classifier_name, thres, ensemble_type, precision, recall, accuracy, fmeasure, roc_auc, ba);
		
	 }// for thres

} catch (Exception e) 
{ 	e.printStackTrace();  }

}
	*/


//This function is used to train and test a using a given classifier
public void within_pred_boosting(Classifier mo, String ensemble_type, int itr) 
{
Evaluation evaluation = null;

AdaBoostM1 model =  new AdaBoostM1();	 //bOOSTING
model.setClassifier(mo);  //bOOSTING
model.setNumIterations(20); //bOOSTING


int len=  mo.getClass().getName().toString().split("\\.").length;
String classifier_name =  mo.getClass().getName().toString().split("\\.")[len-1];

try
{		
  model.buildClassifier(trains);
	evaluation= new Evaluation(trains);
	
	
	int number_of_threshold= 0;
	for(double thres=0.0; thres<=1.0; thres=thres+0.01)
	 {
		double tp=0.0, fp=0.0, tn =0.0,fn=0.0;
		
		for (int j = 0; j < tests.numInstances(); j++) 
		 {
		     
			double score[] ;
			Instance curr  =  tests.instance(j);  
			double actual = curr.classValue();
		  
			score= model.distributionForInstance(curr);
			
			
			 double predicted = 0;
		     if ( score[1] <= thres) 
		     {   predicted = 0;} 
		     else 
		     { 	 predicted = 1 ;}
			 
		     if (actual == 1) 
		       {
			      if (predicted == 1) 
			      { 			       tp++;			      } 
			      else
			      { 			       fn++; 			      }
			     }

			 else if (actual == 0)
			   {
			      if (predicted == 0) 
			      { 			       tn++; 			      } 
			      else 
			      { 			       fp++; 			      }
			     }//else if

			
		 }//  for test instances
		
		util6_met ut6 =  new util6_met();
		
		
		
		/*double precision    =   ut6.compute_precision(tp, fp, tn, fn);
		double recall       =   ut6.compute_recall(tp, fp, tn, fn);
		double accuracy     =   ut6.compute_accuracy(tp, fp, tn, fn);
		double fmeasure     =   ut6.compute_fmeasure(tp, fp, tn, fn);
		double roc_auc      =   0.0;// result.areaUnderROC(1)*100;	
		double ba           =   0.0;// write a function for this computation
		
		
		compute_avg_stdev_and_insert(classifier_name, thres, ensemble_type, precision, recall, accuracy, fmeasure, roc_auc, ba);*/
		
		precision[itr][number_of_threshold]=ut6.compute_precision(tp, fp, tn, fn);
		double temp = ut6.compute_precision(tp, fp, tn, fn);
	
		recall[itr][number_of_threshold]= ut6.compute_recall(tp, fp, tn, fn);
		fmeasure[itr][number_of_threshold]=ut6.compute_fmeasure(tp, fp, tn, fn);
		accuracy[itr][number_of_threshold]=ut6.compute_accuracy(tp, fp, tn, fn);
		
		//ebecuase i want roc auc, hence i am doing evaluation here again. although its not required 
		evaluation.evaluateModel(model, tests);
		roc_auc[itr][number_of_threshold] = evaluation.areaUnderROC(1)*100;// call some method here if possible	
		ba[itr][number_of_threshold]=0.0;
		
		 


		no_of_features[itr] =  trains.numAttributes();

		
		number_of_threshold =  number_of_threshold+1;
	 }// for thres
	
	System.out.println("precision ["+itr+"]["+0+"]="+ precision[itr][0]);//+ "  temp="+temp+ " thres= "+ thres + " tp="+ tp+ "  fp"+ fp +" fn="+fn+" tn="+tn);
	

} catch (Exception e) 
{ 	e.printStackTrace();  }

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
public void compute_avg_stdev_and_insert(String classifier_name,  String ensemble_type, double[] precision, double[] recall, double[] accuracy, double[] fmeasure,
		                                double[] roc_auc, double[] ba, double[] features, double thres) 
{

// computes following metrics:
	/*
	 * 1. Precision
	 * 2. Recall
	 * 3. Accuracy
	 * 4. F measure
	 * 5. ROC-AUC
	 * */

		/////		
		double avg_precision = 0.0;
		double avg_recall = 0.0;
		double avg_accuracy = 0.0;
		double avg_fmeasure = 0.0;	
		double avg_roc_auc = 0.0;
		double avg_ba = 0.0;
		
		double std_precision = 0.0;
		double std_recall = 0.0;
		double std_accuracy = 0.0;
		double std_fmeasure = 0.0;
		double std_roc_auc = 0.0;
		double std_ba = 0.0;
		//double total_instances = 0.0;
		
		util6_met  ut = new util6_met();
		
		avg_precision   = ut.compute_mean(precision);
		avg_recall      = ut.compute_mean(recall);
		avg_fmeasure    = ut.compute_mean(fmeasure);
		avg_accuracy    = ut.compute_mean(accuracy);
		avg_roc_auc     = ut.compute_mean(roc_auc);
		avg_ba = 0.0;
		
		std_precision   = ut.compute_stddev(precision);
		std_recall      = ut.compute_stddev(recall);
		std_fmeasure    = ut.compute_stddev(fmeasure);
		std_accuracy    = ut.compute_stddev(accuracy);
		std_roc_auc     = ut.compute_stddev(roc_auc);
		std_ba=0.0;
		
		double avg_features = ut.compute_mean(no_of_features);
		double std_features =  ut.compute_stddev(no_of_features);
		////
		
	
		// System.out.println("model ="+classifier_name +"   Acc = "+ avg_accuracy + "  size="+ pred_10_db.size());
	
		String insert_str =  " insert into "+ result_table +"  values("+ "'"+ source_project+"','"+ source_project+"','"+ classifier_name+"',"+ thres+",'"+ ensemble_type+"',"+ trains.numInstances() + ","+ tests.numInstances()+","
	                       + iterations+","+ avg_features+","+ std_features +","+avg_precision+","+ std_precision+","+ avg_recall+","+ std_recall+","+avg_fmeasure+","+std_fmeasure+","+ avg_accuracy 
	                       +","+std_accuracy+","+ avg_roc_auc+","+ std_roc_auc+","+ avg_ba+","+ std_ba+" )";
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


private void learn_and_insert(Classifier m1, String ensemble_type) 
{
	
System.out.println("Computing for:"+ m1.getClass().getName()+  "  Ensemble:"+ ensemble_type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file(i);			   
				pre_process_data();
				within_pred(m1, ensemble_type, i);			
				
				//System.out.println(clp.result.toSummaryString());			
					
			}
		

		int len=  m1.getClass().getName().toString().split("\\.").length;
		String classifier_name =  m1.getClass().getName().toString().split("\\.")[len-1];
		
		
		// This is the 	
		int threshold_number=0;
		for(double k=0.0; k<=1.0; k=k+0.01)
		{
			double temp_precision[] = new double [iterations];
			double temp_recall[] = new double[iterations];
			double temp_fmeasure[] = new double[iterations];
			double temp_accuracy[] =  new double[iterations];
			double temp_roc_auc[] =  new double[iterations];
			double temp_ba[] = new double[iterations];
			
			for(int l = 0; l<iterations; l++)
			{
			  temp_precision[l]  = precision[l][threshold_number];	
			  temp_recall[l]     = recall[l][threshold_number];	
			  temp_fmeasure[l]   = fmeasure[l][threshold_number];						
			  temp_accuracy[l]   = accuracy[l][threshold_number];						
			  temp_roc_auc[l]    = roc_auc[l][threshold_number];	
			  temp_ba[l]         = ba[l][threshold_number];	
			  
			}// for
			
			compute_avg_stdev_and_insert(classifier_name,  ensemble_type, temp_precision, temp_recall, temp_accuracy, temp_fmeasure, temp_roc_auc, temp_ba,  no_of_features, k);
			
			//clp.compute_avg_stdev_and_insert(classifier_name, ensemble_tech, temp_thres, feature_selection_tech,  p_of_features, temp_precision, temp_recall, temp_accuracy, temp_fmeasure , temp_roc_auc, temp_train_time,temp_test_time );
	   
	      threshold_number = threshold_number +1;
	      
		}// for
		
		
		
		//compute_avg_stdev_and_insert(classifier_name,  ensemble_type, precision, recall, accuracy, fmeasure, roc_auc, ba,  no_of_features);
				  
		 
}


/*
private void learn_and_insert_bagging(Classifier m1, String ensemble_type, double[] precision,
		double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc, double ba[]) 
{
System.out.println("Computing for:"+ m1.getClass().getName()+  "  Ensemble:"+ ensemble_type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file(i);			   
				pre_process_data();
				within_pred_bagging(m1, ensemble_type, i);			
				
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		  // compute_avg_stdev_and_insert("Random Forest", precision, recall, accuracy, fmeasure , roc_auc );	   
}
*/


private void learn_and_insert_bagging(Classifier m1, String ensemble_type) 
{
	
System.out.println("Computing for:"+ m1.getClass().getName()+  "  Ensemble:"+ ensemble_type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file(i);			   
				pre_process_data();
				within_pred_bagging(m1, ensemble_type, i);			
				
				//System.out.println(clp.result.toSummaryString());			
					
			}
		

		int len=  m1.getClass().getName().toString().split("\\.").length;
		String classifier_name =  m1.getClass().getName().toString().split("\\.")[len-1];
		
		
		// This is the 	
		int threshold_number=0;
		for(double k=0.0; k<=1.0; k=k+0.01)
		{
			double temp_precision[] = new double [iterations];
			double temp_recall[] = new double[iterations];
			double temp_fmeasure[] = new double[iterations];
			double temp_accuracy[] =  new double[iterations];
			double temp_roc_auc[] =  new double[iterations];
			double temp_ba[] = new double[iterations];
			
			for(int l = 0; l<iterations; l++)
			{
			  temp_precision[l]  = precision[l][threshold_number];	
			  temp_recall[l]     = recall[l][threshold_number];	
			  temp_fmeasure[l]   = fmeasure[l][threshold_number];						
			  temp_accuracy[l]   = accuracy[l][threshold_number];						
			  temp_roc_auc[l]    = roc_auc[l][threshold_number];	
			  temp_ba[l]         = ba[l][threshold_number];	
			  
			}// for
			
			compute_avg_stdev_and_insert(classifier_name,  ensemble_type, temp_precision, temp_recall, temp_accuracy, temp_fmeasure, temp_roc_auc, temp_ba,  no_of_features, k);
			
			//clp.compute_avg_stdev_and_insert(classifier_name, ensemble_tech, temp_thres, feature_selection_tech,  p_of_features, temp_precision, temp_recall, temp_accuracy, temp_fmeasure , temp_roc_auc, temp_train_time,temp_test_time );
	   
	      threshold_number = threshold_number +1;
	      
		}// for
		
		
		
		//compute_avg_stdev_and_insert(classifier_name,  ensemble_type, precision, recall, accuracy, fmeasure, roc_auc, ba,  no_of_features);
				  
		 
}

/*
private void learn_and_insert_boosting(Classifier m1, String ensemble_type, double[] precision,
		double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc, double ba[]) 
{
System.out.println("Computing for:"+ m1.getClass().getName()+  "  Ensemble:"+ ensemble_type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file(i);			   
				pre_process_data();
				within_pred_boosting(m1, ensemble_type, i);			
				
				//System.out.println(clp.result.toSummaryString());			
					
			}
				  
		  // compute_avg_stdev_and_insert("Random Forest", precision, recall, accuracy, fmeasure , roc_auc );	   
}*/



private void learn_and_insert_boosting(Classifier m1, String ensemble_type) 
{
	
System.out.println("Computing for:"+ m1.getClass().getName()+  "  Ensemble:"+ ensemble_type);  
	
	//\\=========== Decision table=================================//\\			
		for(int i=0; i<iterations; i++)
			 {
			    read_file(i);			   
				pre_process_data();
				within_pred_boosting(m1, ensemble_type, i);			
				
				//System.out.println(clp.result.toSummaryString());			
					
			}
		

		int len=  m1.getClass().getName().toString().split("\\.").length;
		String classifier_name =  m1.getClass().getName().toString().split("\\.")[len-1];
		
		
		// This is the 	
		int threshold_number=0;
		for(double k=0.0; k<=1.0; k=k+0.01)
		{
			double temp_precision[] = new double [iterations];
			double temp_recall[] = new double[iterations];
			double temp_fmeasure[] = new double[iterations];
			double temp_accuracy[] =  new double[iterations];
			double temp_roc_auc[] =  new double[iterations];
			double temp_ba[] = new double[iterations];
			
			for(int l = 0; l<iterations; l++)
			{
			  temp_precision[l]  = precision[l][threshold_number];	
			  temp_recall[l]     = recall[l][threshold_number];	
			  temp_fmeasure[l]   = fmeasure[l][threshold_number];						
			  temp_accuracy[l]   = accuracy[l][threshold_number];						
			  temp_roc_auc[l]    = roc_auc[l][threshold_number];	
			  temp_ba[l]         = ba[l][threshold_number];	
			  
			}// for
			
			compute_avg_stdev_and_insert(classifier_name,  ensemble_type, temp_precision, temp_recall, temp_accuracy, temp_fmeasure, temp_roc_auc, temp_ba,  no_of_features, k);
			
			//clp.compute_avg_stdev_and_insert(classifier_name, ensemble_tech, temp_thres, feature_selection_tech,  p_of_features, temp_precision, temp_recall, temp_accuracy, temp_fmeasure , temp_roc_auc, temp_train_time,temp_test_time );
	   
	      threshold_number = threshold_number +1;
	      
		}// for
		
		
		
		//compute_avg_stdev_and_insert(classifier_name,  ensemble_type, precision, recall, accuracy, fmeasure, roc_auc, ba,  no_of_features);
				  
		 
}


//This is the main function
public static void main(String args[])
{	  	

	  with_log_pred_nb_bn_score_thres_bag_learning clps =  new with_log_pred_nb_bn_score_thres_bag_learning();
	
	  int total_threshold_count = 0;
	  for(double k=0.0; k<=1.0; k=k+0.01)
		{
		 total_threshold_count =  total_threshold_count +1;
		}	
	  
	  clps.precision = new double [clps.iterations][total_threshold_count+1];
	  clps.recall = new double[clps.iterations][total_threshold_count+1];
	  clps.fmeasure = new double[clps.iterations][total_threshold_count+1];
	  clps.accuracy =  new double[clps.iterations][total_threshold_count+1];
	  clps.roc_auc =  new double[clps.iterations][total_threshold_count+1];
	  clps.ba = new double[clps.iterations][total_threshold_count+1];
	   clps.no_of_features =  new double[clps.iterations];
		
	 // SIMPLE MODELS    
	  clps.learn_and_insert(new ADTree(), "none");
	  clps.learn_and_insert(new J48(), "none");
	  clps.learn_and_insert(new Logistic(), "none");
	  clps.learn_and_insert(new RandomForest(), "none");
	  clps.learn_and_insert(new NaiveBayes(), "none");
	  clps.learn_and_insert(new BayesNet(), "none");
	  clps.learn_and_insert(new AdaBoostM1(), "none");
	  clps.learn_and_insert(new SMO(), "none");
	   
	  
	 /* //Bagging models
	  clps.learn_and_insert_bagging(new ADTree(), "Bagging", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_bagging(new J48(), "Bagging", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_bagging(new Logistic(), "Bagging", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_bagging(new RandomForest() , "Bagging", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_bagging(new NaiveBayes(), "Bagging", precision, recall, accuracy,fmeasure,roc_auc, ba); 
	  clps.learn_and_insert_bagging(new BayesNet(), "Bagging", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_bagging(new AdaBoostM1(), "Bagging", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_bagging(new SMO(), "Bagging", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  
	  
	  // Boosting
	  clps.learn_and_insert_boosting(new ADTree(), "Boosting", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_boosting(new J48(), "Boosting", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_boosting(new Logistic(), "Boosting", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_boosting(new RandomForest() , "Boosting", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_boosting(new NaiveBayes(), "Boosting", precision, recall, accuracy,fmeasure,roc_auc, ba); 
	  clps.learn_and_insert_boosting(new BayesNet(), "Boosting", precision, recall, accuracy,fmeasure,roc_auc, ba);
	 clps.learn_and_insert_boosting(new AdaBoostM1(), "Boosting", precision, recall, accuracy,fmeasure,roc_auc, ba);
	  clps.learn_and_insert_boosting(new SMO(), "Boosting", precision, recall, accuracy,fmeasure,roc_auc, ba);*/
	  
	  
	
	  
	  
     }//main	
	
}// classs




