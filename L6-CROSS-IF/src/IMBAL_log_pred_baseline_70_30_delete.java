import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.ADTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;



/*
 * @Author: Sangeeta
 * 1. This is the simple log prediction code that is used to predict logging using baseline classifier
 * 2. Version  =  baseline 
 * */
public class IMBAL_log_pred_baseline_70_30_delete
{

	///*
	 String path = "E:\\Sangeeta\\Research\\L6-CROSS-IF\\dataset\\";
	 String user_name =  "sangeetal";
	 String password = "sangeetal";
	 String url = "jdbc:mysql://localhost:3307/";
	 String driver = "com.mysql.jdbc.Driver"; 
	  
	// */
	
	/*
	String path = "F:\\Research\\L5IMBAL\\dataset\\";  Change the path + database+ password
	String user_name =  "root";
	String password = "1234";
	String url = "jdbc:mysql://localhost:3306/";
	String driver = "com.mysql.jdbc.Driver";
	//*/
	 

	int iterations=10;	
	//String type = "catch";
	String type ="if";

	String source_project="tomcat";	
	//String source_project="cloudstack";	
	//String source_project="hd";
	
	
	String db_name ="logging6_crossif";
	String result_table = "result_baseline_"+type;

	
	// we are using balanced files for with-in project logging prediction		
   	//String train_file_path = path+source_project+"-arff"+"\\" +type+"\\train";
   	//String test_file_path = path +source_project +"-arff"+"\\"+type+"\\test";
		
	//DataSource trainsource;

	DataSource allsource;
	DataSource trainsource;
	DataSource testsource;
	
	String source_file_path = path+"L6-CROSS-IF\\dataset\\"+source_project+"-arff\\"+type+"\\"+source_project+"_"+type+"_all_features.arff";		


	
	Instances trains;
	Instances tests;
	
	Evaluation result;
		
	int instance_count_train = 0;
	int instance_count_test= 0;
	 

	//Connection conn=null;	
	//java.sql.Statement stmt = null;
   
	double precision[];
	double recall[];
	double fmeasure[];
	double accuracy[];
	double roc_auc[];
	
	long trainbegin ;
	long trainend ;
	long testbegin ;
	long testend ;
	
	long train_time[] ;
	long test_time[];
	
	double no_of_features[];
	
	// This function uses dataset from the ARFF files
	public void read_file(int i)
	 { 
		try 
			{
			
				/*train_data_source = new DataSource(train_file_path+"_"+i+"\\k1\\"+source_project+"_train.arff");
				//System.out.println("File path = "+ train_file_path+"_"+i+"\\k1\\"+source_project+"_train.arff");
				trains = train_data_source.getDataSet();
				trains.setClassIndex(0);
				
				
				test_data_source = new DataSource(test_file_path+"_"+i+"\\"+source_project+"_test.arff");
				tests = test_data_source.getDataSet();
				tests.setClassIndex(0);	*/
			
			allsource = new DataSource(source_file_path);
			Instances all_data = allsource.getDataSet();
			all_data.randomize(new java.util.Random(i));
			
			   
			int trainSize = (int) Math.round(all_data.numInstances() * 0.7);
			int testSize = all_data.numInstances() - trainSize;
			
			trains = new Instances(all_data, 0, trainSize);
			tests = new Instances(all_data, trainSize, testSize);
			
			
			trains.setClassIndex(0);		
			tests.setClassIndex(0);
			
								
				
				instance_count_train = trains.numInstances();
				instance_count_test = tests.numInstances();
				
				System.out.println("Instance count Train ="+ instance_count_train+"   Instance count Test="+ instance_count_test);// + "  Instance count target="+ instance_count_target);
		    
			} catch (Exception e) 
			{
			
				e.printStackTrace();
			}	  
			
		}	
	

// This function is used to pre-process the dataset
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
	     	  

	 	     //2. Standarize  (not normalize because normalization is affected by outliers very easily)   	  
	     	 // Standardize  std_filter =  new Standardize();
	     	 // std_filter.setInputFormat(trains);
	     	 // trains= Filter.useFilter(trains,std_filter);  
	     	  
	     	 // tests =  Filter.useFilter(tests, std_filter);
	     	
	 	     //3. Discretizations
	     	 // Discretize dfilter = new Discretize();
		      //dfilter.setInputFormat(trains);
		     // trains = Filter.useFilter(trains, dfilter);
		      
		      //tests = Filter.useFilter(tests, dfilter);
		      
		
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

}
	
/*
//This method will divide the data in =to two parts: Not used i this work
public void create_train_and_test_split(double train_size, double test_size) 
{
	all_data.randomize(new java.util.Random(0));
	int trainSize = (int) Math.round(all_data.numInstances() * train_size);
	int testSize = all_data.numInstances() - trainSize;
	trains = new Instances(all_data, 0, trainSize);
	tests = new Instances(all_data, trainSize, testSize);

}*/


// This function is used to train and test a using a given classifier
/*public Evaluation pred(Classifier model) 
{
	Evaluation evaluation = null;
	
	try {
	      
		evaluation= new Evaluation(trains);		
		model.buildClassifier(trains);
		evaluation.evaluateModel(model, tests);
	
	
	} catch (Exception e) {
	
		e.printStackTrace();
	}

	return evaluation;
	
	//http://www.programcreek.com/2013/01/a-simple-machine-learning-example-in-java/
}*/

public Evaluation pred2(Classifier model, double thres, int itr) 
{
	Evaluation evaluation = null;
	double tp=0.0, fp=0.0, tn =0.0,fn=0.0;
	
	try {
	      
		trainbegin = System.currentTimeMillis();
		evaluation= new Evaluation(trains);		
		model.buildClassifier(trains);
		
		trainend = System.currentTimeMillis();
		
		//evaluation.evaluateModel(model, tests);	
		testbegin = System.currentTimeMillis();
		for (int j = 0; j < tests.numInstances(); j++) 
		 {
			     
			 double score[] ;
			 Instance curr  =  tests.instance(j);  
			 double actual = curr.classValue();
			 
			  
			score= model.distributionForInstance(curr);
				 
			 
			 // Find index of the model giving maximum value for the test instance
			 
			 double predicted = 0;
		     if ( score[1] <= thres) 
		     {
		      predicted = 0;
		     } else 
		     {
		      predicted = 1;
		     }
			 
		     if (actual == 1) 
		       {
			      if (predicted == 1) 
			      {
			       tp++;
			      } else
			      {
			       fn++;
			      }
			     }

			 else if (actual == 0)
			   {
			      if (predicted == 0) 
			      {
			       tn++;
			      } else 
			      {
			       fp++;
			      }
			     }//else if

			 //System.out.println("tp="+ tp+ "  fp"+ fp +" fn="+fn+" tn="+tn);
			 
		 }//for

		
	 testend = System.currentTimeMillis();

	 util6_met ut =  new util6_met();
	 
	 precision[itr]=ut.compute_precision(tp, fp, tn, fn);
	  recall[itr]= ut.compute_recall(tp, fp, tn, fn);
	fmeasure[itr]=ut.compute_fmeasure(tp, fp, tn, fn);
	accuracy[itr]=ut.compute_accuracy(tp, fp, tn, fn);
	roc_auc[itr] =0.0;// call some method here if possible	
	
	train_time[itr] = trainend -trainbegin;
	test_time[itr] = testend-testbegin;
	
	no_of_features[itr] =  trains.numAttributes();

		//System.out.println("Pre="+ precision[]+"  rec="+ recall+"   fm="+ fmeasure+ "  acc="+ accuracy);
	
	
	} catch (Exception e) {
	
		e.printStackTrace();
	}

	return evaluation;
	
	//http://www.programcreek.com/2013/01/a-simple-machine-learning-example-in-java/
}



public Connection initdb(String db_name)
{
	Connection conn= null;
	
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


// This method computes the average value  and std. deviation and inserts them in a db
public void compute_avg_stdev_and_insert(String classifier_name, double thres, double[] precision, double[] recall, double[] accuracy, double[] fmeasure, double[] roc_auc , long train_time[], long test_time[]) 
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
		
		double avg_train_time= ut.compute_time_avg(train_time);	
		double avg_test_time = ut.compute_time_avg(test_time);
		double std_train_time= ut.compute_time_std(train_time);
		double std_test_time =ut.compute_time_std(test_time);
			
		double avg_features = ut.compute_mean(no_of_features);
		double std_features = ut.compute_stddev(no_of_features);
		
	   // System.out.println("model ="+classifier_name +"   Acc = "+ avg_accuracy + "  size="+ pred_10_db.size());
		
		String insert_str =  " insert into "+ result_table +"  values("+ "'"+ source_project+"','"+ "same_as_source" +"','"+ classifier_name+"',"+thres+","+ trains.numInstances() + ","+ tests.numInstances()+","
		                       + iterations+","+avg_features+ ","+ std_features +","+avg_precision+","+ std_precision+","+ avg_recall+","+ std_recall+","+avg_fmeasure+","+std_fmeasure+","+ avg_accuracy 
		                       +","+std_accuracy+","+ avg_roc_auc+","+ std_roc_auc+ ","+avg_train_time+ ","+std_train_time+","+ avg_test_time+","+ std_test_time+ " )";
		
		System.out.println("Inserting="+ insert_str);
		
		Connection conn2 = initdb(db_name);
		if(conn2==null)
		{
			System.out.println(" Databasse connection is null");
			
		}
		
		try 
		{
			Statement stmt2 = conn2.createStatement();
			stmt2.executeUpdate(insert_str);
			stmt2.close();
			conn2.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	
}

// This is the function created to store the files to help in debugging
  public void save_file_temp_location(Instances trains2, Instances tests2)
  	{
	 
	  try
	  {
	       ArffSaver saver = new ArffSaver();
           saver.setInstances(trains);
       
		   saver.setFile(new File("F:\\result\\logging5_imbal_logg_pred_baseline.arff"));
		
		   saver.writeBatch();
	
	 } catch (IOException e) 
	{
	
		e.printStackTrace();
	}
       
}


//This is the main function
public static void main(String args[])
	{
	
	  
	  Classifier models [] = {  new RandomForest(),
			                   // new Logistic(),
			  					new J48(),
	                           // new RandomTree(),
	                            //new ZeroR(),
	                           // new DecisionTable(),
	                           // new AdaBoostM1(),
	                          //  new ADTree(),
	                           // new RBFNetwork()  
			  					new SMO()
	                            };
	 
		IMBAL_log_pred_baseline_70_30_delete clp = new IMBAL_log_pred_baseline_70_30_delete();
		
		
		// Length of models
		for(int j=0; j<models.length; j++)
		{
			
			String classifier_name =  models[j].getClass().getSimpleName();
			
			for(double thres=0.5; thres==0.5; thres=thres+0.1)
			{
				clp.precision   = new double[clp.iterations];
				clp.recall      = new double[clp.iterations];
				clp.accuracy    = new double[clp.iterations];
				clp.fmeasure    = new double[clp.iterations];	
				clp.roc_auc     = new double[clp.iterations];
			    
				clp.train_time= new long[clp.iterations];
				clp.test_time= new long[clp.iterations];
				
				clp.no_of_features = new double[clp.iterations];
			
				for(int i=0; i<clp.iterations; i++)
					{
				    	clp.read_file(i+1);
				   
				    	clp.pre_process_data();
					
				    	clp.result = clp.pred2(models[j], thres,i);				
					
				    	/*clp.precision[i]         =   clp.result.precision(1)*100;
				    	clp.recall[i]            =   clp.result.recall(1)*100;
				    	clp.accuracy[i]          =   clp.result.pctCorrect(); //not required to multiply by 100, it is already in percentage
				    	clp.fmeasure[i]          =   clp.result.fMeasure(1)*100;
				    	clp.roc_auc[i]           =   clp.result.areaUnderROC(1)*100;*/			
					
						
					}
					  
				
			   clp.compute_avg_stdev_and_insert(classifier_name, thres, clp.precision, clp.recall, clp.accuracy, clp.fmeasure , clp.roc_auc, clp.train_time, clp.test_time );
			   
			} // thres
		}		
		
		
	}

	
}


