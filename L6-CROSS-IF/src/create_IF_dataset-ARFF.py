import MySQLdb
import numpy as np

import utill6


"""=====================================================================================================
@ Author: Sangeeta
@Uses:
1. This file will be used to create dataset from the main training table "project_Training6_IF_isec.java
2. It will create 11 ARFF Files

    a. One having all the instances present in the main table
======================================================================================================"""

#Project
#"""
project= "tomcat"
title = 'Tomcat'
source_project = "tomcat"
target_project = "cloudstack"
#target_project ="hd"
#"""

"""
project =  "cloudstack"
title = 'CloudStack'
source_project = "cloudstack"
target_project = "tomcat"
#target_project = "hd"
#"""

"""
project =  "hd"
title = 'Hadoop'
source_project = "hd"
target_project = "tomcat"
target_project = "cloudstack"
#"""

#"""
port=3306
user="root"
password="1234"
database="logging6_crossif"
main_source_table = project+"_if_training6_crossif"  # from this table we have to take the data
source_table_with_nb_bn_score = project+"_if_training6_nb_bn_score"
path = "F:\\Research\\L6-CROSS-IF\\dataset\\"
all_features_db_file_path=path+project+"-arff\\if\\"+project+"_if_all_features.arff"
num_features_db_file_path=path+project+"-arff\\if\\"+project+"_if_num_features.arff"
bool_features_db_file_path=path+project+"-arff\\if\\"+project+"_if_bool_features.arff"
text_features_db_file_path = path+ project+"-arff\\if\\"+project+"_if_text_features.arff"
all_features_with_in_nb_bn_score_file_path  =  path+ project +"-arff\\if\\"+ project+"_if_with_in_nb_bn_score.arff"
all_features_cross_nb_bn_score_file_path  =  path+ source_project +"-arff\\if\\"+ source_project+"_to_"+target_project+"_if_cross_nb_bn_score.arff"


if_expr_text_features_db_file_path=path+project+"-arff\\if\\"+project+"_if_expr_text_features.arff"
till_if_log_level_text_features_db_file_path = path+project+"-arff\\if\\"+project+"_till_if_log_level_text_features.arff"
operators_till_if_text_features_db_file_path = path+project+"-arff\\if\\"+project+"_till_if_log_level_text_featuress.arff"
variables_till_if_text_features_db_file_path = path+project+"-arff\\if\\"+project+"_till_if_log_level_text_features.arff"
method_call_names_till_if_text_features_db_file_path = path+project+"-arff\\if\\"+project+"_till_if_log_level_text_features.arff"
method_param_type_text_features_db_file_path = path+project+"-arff\\if\\"+project+"_till_if_log_level_text_features.arff"
method_param_name_text_features_db_file_path = path+project+"-arff\\if\\"+project+"_till_if_log_level_text_features.arff"
package_name_text_features_db_file_path =      path+project+"-arff\\if\\"+project+"till_if_log_level_text_features.arff"
class_name_text_features_db_file_path =        path+project+"-arff\\if\\"+project+"_till_if_log_level_text_features.arff"
method_name_text_features_db_file_path=        path+project+"-arff\\if\\"+project+"_till_if_log_level_text_features.arff"
"""

port=3307
user="sangeetal"
password="sangeetal"
database="logging6_crossif"
main_source_table = project+"_if_training6_crossif"  # from this table we have to take the data
source_table_with_nb_bn_score = project+"_if_training6_nb_bn_score"
path = "E:\\Sangeeta\\Research\\L6-CROSS-IF\\dataset\\"
all_features_db_file_path=path+project+"-arff\\if\\"+project+"_if_all_features.arff"
num_features_db_file_path=path+project+"-arff\\if\\"+project+"_if_num_features.arff"
bool_features_db_file_path=path+project+"-arff\\if\\"+project+"_if_bool_features.arff"
text_features_db_file_path = path+ project+"-arff\\if\\"+project+"_if_text_features.arff"
all_features_with_in_nb_bn_score_file_path  =  path+ project +"-arff\\if\\"+ project+"_if_with_in_nb_bn_score.arff"
all_features_cross_nb_bn_score_file_path  =  path+ source_project +"-arff\\if\\"+ source_project+"_to_"+target_project+"_if_cross_nb_bn_score.arff"

#"""


db1= MySQLdb.connect(host="localhost",user=user, passwd=password, db=database, port=port)
select_cursor = db1.cursor()
insert_cursor = db1.cursor()

#=======================================================#
#  @Uses:Write_header() is a function that is used toinsert
# the ARFF header in the file.
#=======================================================#
def write_header_all_features(file_obj,relation_name):
    
    file_obj.write("@relation    "  + relation_name+"\n" )
    file_obj.write("@attribute is_if_logged {0,1}  "+"\n")    
    file_obj.write("@attribute loc_till_if numeric "+"\n")
    file_obj.write("@attribute is_till_if_logged {0,1} "+"\n")
    file_obj.write("@attribute till_if_log_count numeric "+"\n")
    file_obj.write("@attribute operators_count_till_if numeric "+"\n")
    file_obj.write("@attribute variables_count_till_if numeric "+"\n")
    file_obj.write("@attribute method_call_count_till_if numeric "+"\n")
    file_obj.write("@attribute is_return_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_count_in_till_if numeric "+"\n")
    file_obj.write("@attribute is_assert_till_if {0,1} "+"\n")
    file_obj.write("@attribute is_method_have_param {0,1} "+"\n")
    file_obj.write("@attribute method_param_count numeric "+"\n")
    file_obj.write("@attribute is_return_in_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_if {0,1} "+"\n")
    file_obj.write("@attribute is_assert_if {0,1} "+"\n")
    file_obj.write("@attribute is_null_condition_if {0,1} "+"\n")
    file_obj.write("@attribute is_instance_of_condition_if {0,1} "+"\n")
    file_obj.write("@attribute all_text_features_cleaned string "+"\n")
        
    file_obj.write("\n")
    file_obj.write("@data " +"\n")
  
  
  
 #================================================================#
#  @Uses:Write_header() is a function that is used  [NUM Features]
# the ARFF header in the file.
#================================================================#
def write_header_num_features(file_obj,relation_name):
    
    file_obj.write("@relation    "  + relation_name+"\n" )
    file_obj.write("@attribute is_if_logged {0,1}  "+"\n")    
    file_obj.write("@attribute loc_till_if numeric "+"\n")
    #file_obj.write("@attribute is_till_if_logged {0,1} "+"\n")
    file_obj.write("@attribute till_if_log_count numeric "+"\n")
    file_obj.write("@attribute operators_count_till_if numeric "+"\n")
    file_obj.write("@attribute variables_count_till_if numeric "+"\n")
    file_obj.write("@attribute method_call_count_till_if numeric "+"\n")
    #file_obj.write("@attribute is_return_in_till_if {0,1} "+"\n")
    #file_obj.write("@attribute throw_throws_till_if {0,1} "+"\n")
    #file_obj.write("@attribute if_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_count_in_till_if numeric "+"\n")
    #file_obj.write("@attribute is_assert_till_if {0,1} "+"\n")
    #file_obj.write("@attribute is_method_have_param {0,1} "+"\n")
    file_obj.write("@attribute method_param_count numeric "+"\n")
    #file_obj.write("@attribute is_return_in_if {0,1} "+"\n")
    #file_obj.write("@attribute throw_throws_if {0,1} "+"\n")
    #file_obj.write("@attribute is_assert_if {0,1} "+"\n")
    #file_obj.write("@attribute is_null_condition_if {0,1} "+"\n")
    #file_obj.write("@attribute is_instance_of_condition_if {0,1} "+"\n")
    #file_obj.write("@attribute all_text_features_cleaned string "+"\n")
        
    file_obj.write("\n")
    file_obj.write("@data " +"\n")
       
  
 #================================================================#
#  @Uses:Write_header() is a function that is used [BOOL Features]
# the ARFF header in the file.
#===============================================================#
def write_header_bool_features(file_obj,relation_name):
    
    file_obj.write("@relation    "  + relation_name+"\n" )
    file_obj.write("@attribute is_if_logged {0,1}  "+"\n")    
    #file_obj.write("@attribute loc_till_if numeric "+"\n")
    file_obj.write("@attribute is_till_if_logged {0,1} "+"\n")
    #file_obj.write("@attribute till_if_log_count numeric "+"\n")
    #file_obj.write("@attribute operators_count_till_if numeric "+"\n")
    #file_obj.write("@attribute variables_count_till_if numeric "+"\n")
    #file_obj.write("@attribute method_call_count_till_if numeric "+"\n")
    file_obj.write("@attribute is_return_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_in_till_if {0,1} "+"\n")
    #file_obj.write("@attribute if_count_in_till_if numeric "+"\n")
    file_obj.write("@attribute is_assert_till_if {0,1} "+"\n")
    file_obj.write("@attribute is_method_have_param {0,1} "+"\n")
    #file_obj.write("@attribute method_param_count numeric "+"\n")
    file_obj.write("@attribute is_return_in_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_if {0,1} "+"\n")
    file_obj.write("@attribute is_assert_if {0,1} "+"\n")
    file_obj.write("@attribute is_null_condition_if {0,1} "+"\n")
    file_obj.write("@attribute is_instance_of_condition_if {0,1} "+"\n")
    #file_obj.write("@attribute all_text_features_cleaned string "+"\n")
        
    file_obj.write("\n")
    file_obj.write("@data " +"\n")
       
  
  
#=======================================================#
#  @Uses:Write_header() is a function that is used to insert
# the ARFF header in the file.
#=======================================================#
def write_header_text_features(file_obj,relation_name):
    
    file_obj.write("@relation    "  + relation_name+"\n" )
    file_obj.write("@attribute is_if_logged {0,1}  "+"\n")    
    file_obj.write("@attribute all_text_features_cleaned string "+"\n")
        
    file_obj.write("\n")
    file_obj.write("@data " +"\n")
  
  

#=======================================================#
#  @ write header including bn_bn_score
# 
#=======================================================#
def write_header_all_features_with_in_nb_bn_score(file_obj,relation_name):
    
    nb_score_string = source_project +"_to_"+ source_project+ "_nb_score"
    bn_score_string = source_project +"_to_"+ source_project+ "_bn_score"
    
    file_obj.write("@relation    "  + relation_name+"\n" )
    file_obj.write("@attribute is_if_logged {0,1}  "+"\n")    
    file_obj.write("@attribute loc_till_if numeric "+"\n")
    file_obj.write("@attribute is_till_if_logged {0,1} "+"\n")
    file_obj.write("@attribute till_if_log_count numeric "+"\n")
    file_obj.write("@attribute operators_count_till_if numeric "+"\n")
    file_obj.write("@attribute variables_count_till_if numeric "+"\n")
    file_obj.write("@attribute method_call_count_till_if numeric "+"\n")
    file_obj.write("@attribute is_return_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_count_in_till_if numeric "+"\n")
    file_obj.write("@attribute is_assert_till_if {0,1} "+"\n")
    file_obj.write("@attribute is_method_have_param {0,1} "+"\n")
    file_obj.write("@attribute method_param_count numeric "+"\n")
    file_obj.write("@attribute is_return_in_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_if {0,1} "+"\n")
    file_obj.write("@attribute is_assert_if {0,1} "+"\n")
    file_obj.write("@attribute is_null_condition_if {0,1} "+"\n")
    file_obj.write("@attribute is_instance_of_condition_if {0,1} "+"\n")
   
    file_obj.write("@attribute "+nb_score_string+"  numeric \n")
    file_obj.write("@attribute "+bn_score_string+ "  numeric \n")
    
    #file_obj.write("@attribute all_text_features_cleaned string "+"\n")
        
    file_obj.write("\n")
    file_obj.write("@data " +"\n")
    
    
  
  

#=======================================================#
#  @ write header including cross project  bn_bn_score
# 
#=======================================================#
def write_header_all_features_cross_nb_bn_score(file_obj,relation_name):
    
    nb_score_string = target_project +"_to_"+ source_project+ "_nb_score"
    bn_score_string = target_project +"_to_"+ source_project+ "_bn_score"
    
    file_obj.write("@relation    "  + relation_name+"\n" )
    file_obj.write("@attribute is_if_logged {0,1}  "+"\n")    
    file_obj.write("@attribute loc_till_if numeric "+"\n")
    file_obj.write("@attribute is_till_if_logged {0,1} "+"\n")
    file_obj.write("@attribute till_if_log_count numeric "+"\n")
    file_obj.write("@attribute operators_count_till_if numeric "+"\n")
    file_obj.write("@attribute variables_count_till_if numeric "+"\n")
    file_obj.write("@attribute method_call_count_till_if numeric "+"\n")
    file_obj.write("@attribute is_return_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_in_till_if {0,1} "+"\n")
    file_obj.write("@attribute if_count_in_till_if numeric "+"\n")
    file_obj.write("@attribute is_assert_till_if {0,1} "+"\n")
    file_obj.write("@attribute is_method_have_param {0,1} "+"\n")
    file_obj.write("@attribute method_param_count numeric "+"\n")
    file_obj.write("@attribute is_return_in_if {0,1} "+"\n")
    file_obj.write("@attribute throw_throws_if {0,1} "+"\n")
    file_obj.write("@attribute is_assert_if {0,1} "+"\n")
    file_obj.write("@attribute is_null_condition_if {0,1} "+"\n")
    file_obj.write("@attribute is_instance_of_condition_if {0,1} "+"\n")
   
    file_obj.write("@attribute "+nb_score_string+"  numeric \n")
    file_obj.write("@attribute "+bn_score_string+ "  numeric \n")
    
    #file_obj.write("@attribute all_text_features_cleaned string "+"\n")
        
    file_obj.write("\n")
    file_obj.write("@data " +"\n")    


## Note: This function is different from other create dataset files  ##
#======================================================================#
#@ Note: This function taks as input the faetures that are in the file
#======================================================================#
def write_header_one_text_features(file_obj,relation_name, text_feature_name):
    
    file_obj.write("@relation    "  + relation_name+"\n" )
    file_obj.write("@attribute is_if_logged {0,1}  "+"\n")    
    file_obj.write("@attribute "+  text_feature_name +"\n")
        
    file_obj.write("\n")
    file_obj.write("@data " +"\n")
  

  
    
#=======================================================#
# @uses: Function to write in file ceate arff files
#=======================================================#
def write_in_file_all_features(file_obj, tuple_val):
        
    t_if_expr                  = tuple_val[0]
    n_loc_till_if              =  tuple_val[1]
    n_is_till_if_logged        = tuple_val[2]
    n_till_if_log_count        = tuple_val[3]
    t_till_if_log_levels       = tuple_val[4]
    t_operators_till_if        = tuple_val[5]
    n_operators_count_till_if  = tuple_val[6]
    t_variables_till_if          = tuple_val[7]
    n_variables_count_till_if    = tuple_val[8]
    t_method_call_names_till_if   =tuple_val[9]
    n_method_call_count_till_if   = tuple_val[10]
    n_is_return_in_till_if        =tuple_val[11]
    n_throw_throws_till_if        =tuple_val[12]
    n_if_in_till_if               =tuple_val[13]
    n_if_count_in_till_if         =tuple_val[14]
    n_is_assert_till_if          =tuple_val[15]
    n_is_method_have_param        =tuple_val[16] 
    t_method_param_type          =tuple_val[17]
    t_method_param_name         =tuple_val[18]
    n_method_param_count        =tuple_val[19]
    n_is_return_in_if           = tuple_val[20]
    n_throw_throws_if          = tuple_val[21]
    n_is_assert_if              =tuple_val[22]
    n_is_null_condition_if          = tuple_val[23] 
    n_is_instance_of_condition_if = tuple_val[24] 
    t_package_name               =tuple_val[25]
    t_class_name                =tuple_val[26]
    t_method_name                =tuple_val[27]
           
    is_if_logged = tuple_val[28]
    
    
    operator_feature =  t_operators_till_if
    
    text_features =      t_if_expr + " "+            t_till_if_log_levels   +" "                  +    t_variables_till_if +" "        +  t_method_call_names_till_if +" "+\
             t_method_param_type + " " +  t_method_param_name +" " +  t_package_name+" "+ t_class_name + " "+ t_method_name         
    
    #Applying camel casing
    text_features = utill6.camel_case_convert(text_features)
    text_features = utill6.remove_stop_words(text_features)
    text_features = utill6.stem_it(text_features)
    
    text_features =  text_features +" " + operator_feature
    
    text_features =  text_features.strip()
    
    print "writing if:"   
   
    
    #=== write the data in the file=====================#
    write_str =""+ (str)(is_if_logged )+","+  (str)(n_loc_till_if)  +","+ (str)(n_is_till_if_logged ) +","+ (str)(n_till_if_log_count) +","+(str)( n_operators_count_till_if) +","+ \
    (str)(n_variables_count_till_if) +","+ (str)( n_method_call_count_till_if)  +","+ (str)(n_is_return_in_till_if)+","+ (str)(n_throw_throws_till_if)  +","+ \
    (str)(n_if_in_till_if) +","+ (str)(n_if_count_in_till_if) +","+ (str)(n_is_assert_till_if ) +","+  (str)(n_is_method_have_param )      +","+ \
    (str)( n_method_param_count)  +","+ (str)(n_is_return_in_if ) +","+ (str)(n_throw_throws_if)  +","+    (str)( n_is_assert_if  )           +","+ \
    (str)(n_is_null_condition_if)  +","+    (str)( n_is_instance_of_condition_if) +",'"+ text_features+"'"
      
    # ==write in the file======#  
    file_obj.write(write_str+"\n")       
            
    #target.append(0)  Removing from here moving up                  
    #db1.commit()           
    

#=======================================================#
# @uses: Function to write in file ceate arff files
#=======================================================#
def write_in_file_num_features(file_obj, tuple_val):

   # t_if_expr                  = tuple_val[0]
    n_loc_till_if              =  tuple_val[1]
    #n_is_till_if_logged        = tuple_val[2]
    n_till_if_log_count        = tuple_val[3]
    #t_till_if_log_levels       = tuple_val[4]
    #t_operators_till_if        = tuple_val[5]
    n_operators_count_till_if  = tuple_val[6]
    #t_variables_till_if          = tuple_val[7]
    n_variables_count_till_if    = tuple_val[8]
    #t_method_call_names_till_if   =tuple_val[9]
    n_method_call_count_till_if   = tuple_val[10]
    #n_is_return_in_till_if        =tuple_val[11]
    #n_throw_throws_till_if        =tuple_val[12]
    #n_if_in_till_if               =tuple_val[13]
    n_if_count_in_till_if         =tuple_val[14]
    #n_is_assert_till_if          =tuple_val[15]
    #n_is_method_have_param        =tuple_val[16] 
    #t_method_param_type          =tuple_val[17]
    #t_method_param_name         =tuple_val[18]
    n_method_param_count        =tuple_val[19]
    #n_is_return_in_if           = tuple_val[20]
    #n_throw_throws_if          = tuple_val[21]
    #n_is_assert_if              =tuple_val[22]
    #n_is_null_condition_if          = tuple_val[23] 
    #n_is_instance_of_condition_if = tuple_val[24] 
    #t_package_name               =tuple_val[25]
    #t_class_name                =tuple_val[26]
    #t_method_name                =tuple_val[27]
           
    is_if_logged = tuple_val[28]
    
    
    #operator_feature =  t_operators_till_if
    
    #text_features =      t_if_expr + " "+            t_till_if_log_levels   +" "                  +    t_variables_till_if +" "        +  t_method_call_names_till_if +" "+\
    #         t_method_param_type + " " +  t_method_param_name +" " +  t_package_name+" "+ t_class_name + " "+ t_method_name         
    
    #Applying camel casing
    #text_features = utill6_isec.camel_case_convert(text_features)
    #text_features = utill6_isec.remove_stop_words(text_features)
    #text_features = utill6_isec.stem_it(text_features)
    
    #text_features =  text_features +" " + operator_feature
    
    #text_features =  text_features.strip()
    
    print "writing if: numfeatures"   
   
    
    #=== write the data in the file=====================#
   # write_str =""+ (str)(is_if_logged )+","+  (str)(n_loc_till_if)  +","+ (str)(n_is_till_if_logged ) +","+ (str)(n_till_if_log_count) +","+(str)( n_operators_count_till_if) +","+ \
   # (str)(n_variables_count_till_if) +","+ (str)( n_method_call_count_till_if)  +","+ (str)(n_is_return_in_till_if)+","+ (str)(n_throw_throws_till_if)  +","+ \
   # (str)(n_if_in_till_if) +","+ (str)(n_if_count_in_till_if) +","+ (str)(n_is_assert_till_if ) +","+  (str)(n_is_method_have_param )      +","+ \
   # (str)( n_method_param_count)  +","+ (str)(n_is_return_in_if ) +","+ (str)(n_throw_throws_if)  +","+    (str)( n_is_assert_if  )           +","+ \
   # (str)(n_is_null_condition_if)  +","+    (str)( n_is_instance_of_condition_if) +",'"+ text_features+"')"
   
   
    write_str =""+ (str)(is_if_logged )+","+  (str)(n_loc_till_if)  +","+  (str)(n_till_if_log_count) +","+(str)( n_operators_count_till_if) +","+ \
    (str)(n_variables_count_till_if) +","+ (str)( n_method_call_count_till_if)  +","+ \
    (str)(n_if_count_in_till_if) +","+ \
    (str)( n_method_param_count)
     
      
    # ==write in the file======#  
    file_obj.write(write_str+"\n")       
            
    #target.append(0)  Removing from here moving up                  
    #db1.commit()           
    


#=======================================================#
# @uses: Function to write in file ceate arff files
#=======================================================#
def write_in_file_bool_features(file_obj, tuple_val):
      
   # t_if_expr                  = tuple_val[0]
    #n_loc_till_if              =  tuple_val[1]
    n_is_till_if_logged        = tuple_val[2]
    #n_till_if_log_count        = tuple_val[3]
    #t_till_if_log_levels       = tuple_val[4]
    #t_operators_till_if        = tuple_val[5]
    #n_operators_count_till_if  = tuple_val[6]
    #t_variables_till_if          = tuple_val[7]
    #n_variables_count_till_if    = tuple_val[8]
    #t_method_call_names_till_if   =tuple_val[9]
    #n_method_call_count_till_if   = tuple_val[10]
    n_is_return_in_till_if        =tuple_val[11]
    n_throw_throws_till_if        =tuple_val[12]
    n_if_in_till_if               =tuple_val[13]
    #n_if_count_in_till_if         =tuple_val[14]
    n_is_assert_till_if          =tuple_val[15]
    n_is_method_have_param        =tuple_val[16] 
    #t_method_param_type          =tuple_val[17]
    #t_method_param_name         =tuple_val[18]
    #n_method_param_count        =tuple_val[19]
    n_is_return_in_if           = tuple_val[20]
    n_throw_throws_if          = tuple_val[21]
    n_is_assert_if              =tuple_val[22]
    n_is_null_condition_if          = tuple_val[23] 
    n_is_instance_of_condition_if = tuple_val[24] 
    #t_package_name               =tuple_val[25]
    #t_class_name                =tuple_val[26]
    #t_method_name                =tuple_val[27]
           
    is_if_logged = tuple_val[28]
    
    
    #operator_feature =  t_operators_till_if
    
    #text_features =      t_if_expr + " "+            t_till_if_log_levels   +" "                  +    t_variables_till_if +" "        +  t_method_call_names_till_if +" "+\
    #         t_method_param_type + " " +  t_method_param_name +" " +  t_package_name+" "+ t_class_name + " "+ t_method_name         
    
    #Applying camel casing
    #text_features = utill6_isec.camel_case_convert(text_features)
    #text_features = utill6_isec.remove_stop_words(text_features)
    #text_features = utill6_isec.stem_it(text_features)
    
    #text_features =  text_features +" " + operator_feature
    
    #text_features =  text_features.strip()
    
    print "writing if: bool features"   
   
    
    #=== write the data in the file=====================#
   # write_str =""+ (str)(is_if_logged )+","+  (str)(n_loc_till_if)  +","+ (str)(n_is_till_if_logged ) +","+ (str)(n_till_if_log_count) +","+(str)( n_operators_count_till_if) +","+ \
   # (str)(n_variables_count_till_if) +","+ (str)( n_method_call_count_till_if)  +","+ (str)(n_is_return_in_till_if)+","+ (str)(n_throw_throws_till_if)  +","+ \
   # (str)(n_if_in_till_if) +","+ (str)(n_if_count_in_till_if) +","+ (str)(n_is_assert_till_if ) +","+  (str)(n_is_method_have_param )      +","+ \
   # (str)( n_method_param_count)  +","+ (str)(n_is_return_in_if ) +","+ (str)(n_throw_throws_if)  +","+    (str)( n_is_assert_if  )           +","+ \
   # (str)(n_is_null_condition_if)  +","+    (str)( n_is_instance_of_condition_if) +",'"+ text_features+"')"
   
   
    write_str =""+ (str)(is_if_logged ) +","+ (str)(n_is_till_if_logged )  +","+ \
    (str)(n_is_return_in_till_if)+","+ (str)(n_throw_throws_till_if)  +","+ \
    (str)(n_if_in_till_if) +","+ (str)(n_is_assert_till_if ) +","+  (str)(n_is_method_have_param )      +","+ \
    (str)(n_is_return_in_if ) +","+ (str)(n_throw_throws_if)  +","+    (str)( n_is_assert_if  )           +","+ \
    (str)(n_is_null_condition_if)  +","+    (str)( n_is_instance_of_condition_if)    
     
      
    # ==write in the file======#  
    file_obj.write(write_str+"\n")       
            
    #target.append(0)  Removing from here moving up                  
    #db1.commit()           
    

#=======================================================#
# @uses: Function to write in file create arff files
#=======================================================#
def write_in_file_text_features(file_obj, tuple_val):
    
    t_if_expr                  = tuple_val[0]
    #n_loc_till_if              =  tuple_val[1]
    #n_is_till_if_logged        = tuple_val[2]
    #n_till_if_log_count        = tuple_val[3]
    t_till_if_log_levels       = tuple_val[4]
    t_operators_till_if        = tuple_val[5]
    #n_operators_count_till_if  = tuple_val[6]
    t_variables_till_if          = tuple_val[7]
    #n_variables_count_till_if    = tuple_val[8]
    t_method_call_names_till_if   =tuple_val[9]
    #n_method_call_count_till_if   = tuple_val[10]
    #n_is_return_in_till_if        =tuple_val[11]
    #n_throw_throws_till_if        =tuple_val[12]
    #n_if_in_till_if               =tuple_val[13]
    #n_if_count_in_till_if         =tuple_val[14]
    #n_is_assert_till_if          =tuple_val[15]
    #n_is_method_have_param        =tuple_val[16] 
    t_method_param_type          =tuple_val[17]
    t_method_param_name         =tuple_val[18]
    #n_method_param_count        =tuple_val[19]
    #n_is_return_in_if           = tuple_val[20]
    #n_throw_throws_if          = tuple_val[21]
    #n_is_assert_if              =tuple_val[22]
    #n_is_null_condition_if          = tuple_val[23] 
    #n_is_instance_of_condition_if = tuple_val[24] 
    t_package_name               =tuple_val[25]
    t_class_name                =tuple_val[26]
    t_method_name                =tuple_val[27]
           
    is_if_logged = tuple_val[28]
    
    
    operator_feature =  t_operators_till_if
    
    text_features =      t_if_expr + " "+            t_till_if_log_levels   +" "                  +    t_variables_till_if +" "        +  t_method_call_names_till_if +" "+\
             t_method_param_type + " " +  t_method_param_name +" " +  t_package_name+" "+ t_class_name + " "+ t_method_name         
    
    #Applying camel casing
    text_features = utill6.camel_case_convert(text_features)
    text_features = utill6.remove_stop_words(text_features)
    text_features = utill6.stem_it(text_features)
    
    text_features =  text_features +" " + operator_feature
    
    text_features =  text_features.strip()
    
    print "writing if:"   
   
    
    #=== write the data in the file=====================#
    write_str =""+ (str)(is_if_logged )+",'"+   text_features+"'"
      
    # ==write in the file======#  
    file_obj.write(write_str+"\n")       
            
    #target.append(0)  Removing from here moving up                  
    #db1.commit()           

   
#==============================================================================#
# @uses: Function to write in file create within files having nb and bn scores
#==============================================================================#
def write_in_file_all_features_with_in_nb_bn_score_file_path(file_obj, tuple_val):
           
    #t_if_expr                  = tuple_val[0]
    n_loc_till_if              =  tuple_val[1]
    n_is_till_if_logged        = tuple_val[2]
    n_till_if_log_count        = tuple_val[3]
    #t_till_if_log_levels       = tuple_val[4]
    #t_operators_till_if        = tuple_val[5]
    n_operators_count_till_if  = tuple_val[6]
    #t_variables_till_if          = tuple_val[7]
    n_variables_count_till_if    = tuple_val[8]
    #t_method_call_names_till_if   =tuple_val[9]
    n_method_call_count_till_if   = tuple_val[10]
    n_is_return_in_till_if        =tuple_val[11]
    n_throw_throws_till_if        =tuple_val[12]
    n_if_in_till_if               =tuple_val[13]
    n_if_count_in_till_if         =tuple_val[14]
    n_is_assert_till_if          =tuple_val[15]
    n_is_method_have_param        =tuple_val[16] 
    #t_method_param_type          =tuple_val[17]
    #t_method_param_name         =tuple_val[18]
    n_method_param_count        =tuple_val[19]
    n_is_return_in_if           = tuple_val[20]
    n_throw_throws_if          = tuple_val[21]
    n_is_assert_if              =tuple_val[22]
    n_is_null_condition_if          = tuple_val[23] 
    n_is_instance_of_condition_if = tuple_val[24] 
    #t_package_name               =tuple_val[25]
    #t_class_name                =tuple_val[26]
    #t_method_name                =tuple_val[27]
           
    is_if_logged = tuple_val[28]
    
    n_source_project_to_source_project_nb_score =  tuple_val[29]
    n_source_project_to_source_project_bn_score =  tuple_val[30]
    
    #operator_feature =  t_operators_till_if
    
    #text_features =      t_if_expr + " "+            t_till_if_log_levels   +" "                  +    t_variables_till_if +" "        +  t_method_call_names_till_if +" "+\
    #         t_method_param_type + " " +  t_method_param_name +" " +  t_package_name+" "+ t_class_name + " "+ t_method_name         
    
    #Applying camel casing
    #text_features = utill6.camel_case_convert(text_features)
    #text_features = utill6.remove_stop_words(text_features)
    #text_features = utill6.stem_it(text_features)
    
    #text_features =  text_features +" " + operator_feature
    
    #text_features =  text_features.strip()
    
    #print "writing if:"   
   
    
    #=== write the data in the file=====================#
    write_str =""+ (str)(is_if_logged )+","+  (str)(n_loc_till_if)  +","+ (str)(n_is_till_if_logged ) +","+ (str)(n_till_if_log_count) +","+(str)( n_operators_count_till_if) +","+ \
    (str)(n_variables_count_till_if) +","+ (str)( n_method_call_count_till_if)  +","+ (str)(n_is_return_in_till_if)+","+ (str)(n_throw_throws_till_if)  +","+ \
    (str)(n_if_in_till_if) +","+ (str)(n_if_count_in_till_if) +","+ (str)(n_is_assert_till_if ) +","+  (str)(n_is_method_have_param )      +","+ \
    (str)( n_method_param_count)  +","+ (str)(n_is_return_in_if ) +","+ (str)(n_throw_throws_if)  +","+    (str)( n_is_assert_if  )           +","+ \
    (str)(n_is_null_condition_if)  +","+    (str)( n_is_instance_of_condition_if) +","+(str)(n_source_project_to_source_project_nb_score) +","+ (str)(n_source_project_to_source_project_bn_score)
      
    # ==write in the file======#  
    file_obj.write(write_str+"\n")       
            
    #target.append(0)  Removing from here moving up                  
    #db1.commit()           


   
#==============================================================================#
# @uses: Function to write in file create cross project having nb and bn scores
#==============================================================================#
def write_in_file_all_features_cross_nb_bn_score_file_path(file_obj, tuple_val):
           
    #t_if_expr                  = tuple_val[0]
    n_loc_till_if              =  tuple_val[1]
    n_is_till_if_logged        = tuple_val[2]
    n_till_if_log_count        = tuple_val[3]
    #t_till_if_log_levels       = tuple_val[4]
    #t_operators_till_if        = tuple_val[5]
    n_operators_count_till_if  = tuple_val[6]
    #t_variables_till_if          = tuple_val[7]
    n_variables_count_till_if    = tuple_val[8]
    #t_method_call_names_till_if   =tuple_val[9]
    n_method_call_count_till_if   = tuple_val[10]
    n_is_return_in_till_if        =tuple_val[11]
    n_throw_throws_till_if        =tuple_val[12]
    n_if_in_till_if               =tuple_val[13]
    n_if_count_in_till_if         =tuple_val[14]
    n_is_assert_till_if          =tuple_val[15]
    n_is_method_have_param        =tuple_val[16] 
    #t_method_param_type          =tuple_val[17]
    #t_method_param_name         =tuple_val[18]
    n_method_param_count        =tuple_val[19]
    n_is_return_in_if           = tuple_val[20]
    n_throw_throws_if          = tuple_val[21]
    n_is_assert_if              =tuple_val[22]
    n_is_null_condition_if          = tuple_val[23] 
    n_is_instance_of_condition_if = tuple_val[24] 
    #t_package_name               =tuple_val[25]
    #t_class_name                =tuple_val[26]
    #t_method_name                =tuple_val[27]
           
    is_if_logged = tuple_val[28]
    
    n_target_project_to_source_project_nb_score =  tuple_val[29]
    n_target_project_to_source_project_bn_score =  tuple_val[30]
    
    #operator_feature =  t_operators_till_if
    
    #text_features =      t_if_expr + " "+            t_till_if_log_levels   +" "                  +    t_variables_till_if +" "        +  t_method_call_names_till_if +" "+\
    #         t_method_param_type + " " +  t_method_param_name +" " +  t_package_name+" "+ t_class_name + " "+ t_method_name         
    
    #Applying camel casing
    #text_features = utill6.camel_case_convert(text_features)
    #text_features = utill6.remove_stop_words(text_features)
    #text_features = utill6.stem_it(text_features)
    
    #text_features =  text_features +" " + operator_feature
    
    #text_features =  text_features.strip()
    
    #print "writing if:"   
   
    
    #=== write the data in the file=====================#
    write_str =""+ (str)(is_if_logged )+","+  (str)(n_loc_till_if)  +","+ (str)(n_is_till_if_logged ) +","+ (str)(n_till_if_log_count) +","+(str)( n_operators_count_till_if) +","+ \
    (str)(n_variables_count_till_if) +","+ (str)( n_method_call_count_till_if)  +","+ (str)(n_is_return_in_till_if)+","+ (str)(n_throw_throws_till_if)  +","+ \
    (str)(n_if_in_till_if) +","+ (str)(n_if_count_in_till_if) +","+ (str)(n_is_assert_till_if ) +","+  (str)(n_is_method_have_param )      +","+ \
    (str)( n_method_param_count)  +","+ (str)(n_is_return_in_if ) +","+ (str)(n_throw_throws_if)  +","+    (str)( n_is_assert_if  )           +","+ \
    (str)(n_is_null_condition_if)  +","+    (str)( n_is_instance_of_condition_if) +","+(str)(n_target_project_to_source_project_nb_score) +","+ (str)(n_target_project_to_source_project_bn_score)
      
    # ==write in the file======#  
    file_obj.write(write_str+"\n")       
            
    #target.append(0)  Removing from here moving up                  
    #db1.commit()           




#=======================#
#  all features  #
#======================#
def create_one_complete_all_features(all_features_db_file_path):
    #===========Read all the if blocks===============================#
   

    str_total_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged\
                       from "+ main_source_table +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!='' "
    
    
    select_cursor.execute(str_total_data)
    total_data = select_cursor.fetchall()


    #===========================================#
    #@ 1. Create the complete database    
    #===========================================#
   
    file_obj =  open(all_features_db_file_path, 'w+')
   
    # 1. Write header in the file
    relation_name =  project +"_if_all_features"
    write_header_all_features(file_obj, relation_name)
    
    #2. write database ibstabces
    for d in total_data:   
        write_in_file_all_features(file_obj, d)
    
    
    file_obj.close()
    
    
#=======================#
#  num features         #
#=======================#
def create_one_complete_num_features(num_features_db_file_path):
    #===========Read all the if blocks===============================#
   
    str_total_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged\
                       from "+ main_source_table +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!='' "
    
    
    select_cursor.execute(str_total_data)
    total_data = select_cursor.fetchall()


    #===========================================#
    #@ 1. Create the complete database    
    #===========================================#
   
    file_obj =  open(num_features_db_file_path, 'w+')
   
    # 1. Write header in the file
    relation_name =  project +"_if_num_features"
    write_header_num_features(file_obj, relation_name)
    
    #2. write database ibstabces
    for d in total_data:   
        write_in_file_num_features(file_obj, d)
    
    
    file_obj.close()
    
#=======================#
#  bool features         #
#=======================#
def create_one_complete_bool_features(bool_features_db_file_path):
    #===========Read all the if blocks===============================#
   
    str_total_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged\
                       from "+ main_source_table +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!='' "
    
    
    select_cursor.execute(str_total_data)
    total_data = select_cursor.fetchall()


    #===========================================#
    #@ 1. Create the complete database    
    #===========================================#
   
    file_obj =  open(bool_features_db_file_path, 'w+')
   
    # 1. Write header in the file
    relation_name =  project +"_if_bool_features"
    write_header_bool_features(file_obj, relation_name)
    
    #2. write database ibstabces
    for d in total_data:   
        write_in_file_bool_features(file_obj, d)
    
    
    file_obj.close()



#=======================#
#  all text features  #
#======================#
def create_one_complete_text_features(text_features_db_file_path):
    #===========Read all the if blocks===============================#
   
   

    str_total_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged\
                       from "+ main_source_table +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!='' "
    
    
    select_cursor.execute(str_total_data)
    total_data = select_cursor.fetchall()


    #===========================================#
    #@ 1. Create the complete database    
    #===========================================#
   
    file_obj =  open(text_features_db_file_path, 'w+')
   
    # 1. Write header in the file
    relation_name =  project +"_if_text_features"
    write_header_text_features(file_obj, relation_name)
    
    #2. write database ibstabces
    for d in total_data:   
        write_in_file_text_features(file_obj, d)
    
    
    file_obj.close()
  
  
#=======================================#
#  all with_in_nb_bn_score features  #
#=========================================#
def create_one_complete_all_features_with_in_nb_bn_score(all_features_with_in_nb_bn_score_file_path):
    #===========Read all the if blocks===============================#
   
    nb_score_string = source_project+"_to_"+ source_project+"_nb_score"
    bn_score_string = source_project+"_to_"+ source_project+"_bn_score"
  

    str_total_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged,"\
                        " "+ nb_score_string+ ","+ bn_score_string +""\
                       " from "+ source_table_with_nb_bn_score +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!='' "
    
    
    print "str string", str_total_data
    select_cursor.execute(str_total_data)
    total_data = select_cursor.fetchall()


    #===========================================#
    #@ 1. Create the complete database    
    #===========================================#
   
    file_obj =  open(all_features_with_in_nb_bn_score_file_path, 'w+')
   
    # 1. Write header in the file
    relation_name =  project +"_if_all_features_with_in_nb_bn_score"
    write_header_all_features_with_in_nb_bn_score(file_obj, relation_name)
    
    #2. write database ibstabces
    for d in total_data:   
        write_in_file_all_features_with_in_nb_bn_score_file_path(file_obj, d)
    
    
    file_obj.close()


 
#=======================================#
#  all with_in_nb_bn_score features  #
#=========================================#

def create_one_complete_all_features_cross_nb_bn_score(all_features_cross_nb_bn_score_file_path):
    #===========Read all the if blocks===============================#
   
    nb_score_string = target_project+"_to_"+ source_project+"_nb_score"
    bn_score_string = target_project+"_to_"+ source_project+"_bn_score"
  

    str_total_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged,"\
                        " "+ nb_score_string+ ","+ bn_score_string +""\
                       " from "+ source_table_with_nb_bn_score +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!='' "
    
    
    print "str string", str_total_data
    select_cursor.execute(str_total_data)
    total_data = select_cursor.fetchall()


    #===========================================#
    #@ 1. Create the complete database    
    #===========================================#
   
    file_obj =  open(all_features_cross_nb_bn_score_file_path, 'w+')
   
    # 1. Write header in the file
    relation_name =  source_project+"_to_"+ target_project +"_if_all_features_cross_nb_bn_score"
    write_header_all_features_cross_nb_bn_score(file_obj, relation_name)
    
    #2. write database ibstabces
    for d in total_data:   
        write_in_file_all_features_cross_nb_bn_score_file_path(file_obj, d)
    
    
    file_obj.close()
    

    
  
#===========================================#
# @ File for one text feature individually  #
#===========================================#    

def create_different_text_features_files(if_expr_text_features_db_file_path, till_if_log_level_text_features_db_file_path,  operators_till_if_text_features_db_file_path, variables_till_if_text_features_db_file_path, 
                            method_call_names_till_if_text_features_db_file_path, method_param_type_text_features_db_file_path, method_param_name_text_features_db_file_path,
                             package_name_text_features_db_file_path, class_name_text_features_db_file_path, method_name_text_features_db_file_path):

    str_total_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged\
                       from "+ main_source_table +" where if_expr not like '%isTraceEnabled()'  and \
                       if_expr not like '%isDebugEnabled()'  and if_expr not like '%isInfoEnabled()' and if_expr not like '%isWarnEnabled()'  \
                       and if_expr not like '%isErrorEnabled()'  and if_expr not like '%isFatalEnabled()'  and if_expr!='' "
                       
    select_cursor.execute(str_total_data)
    total_data = select_cursor.fetchall()


    #===========================================#
    #@ 1. Create the complete database    
    #===========================================#
   
    file_obj_if_expr_text_features =                  open(if_expr_text_features_db_file_path, 'w+')
    file_obj_till_if_log_level_text_features=         open(till_if_log_level_text_features_db_file_path, 'w+')
    file_obj_operators_till_if_text_features =        open(operators_till_if_text_features_db_file_path, 'w+')
    file_obj_variables_till_if_text_features=         open(variables_till_if_text_features_db_file_path, 'w+')
    file_obj_method_call_names_till_if_text_features= open(method_call_names_till_if_text_features_db_file_path, 'w+')
    file_obj_method_param_type_text_features=         open(method_param_type_text_features_db_file_path, 'w+')
    file_obj_method_param_name_text_features=         open(method_param_name_text_features_db_file_path, 'w+')
    file_obj_package_name_text_features=              open(package_name_text_features_db_file_path, 'w+')
    file_obj_class_name_text_features=                open(class_name_text_features_db_file_path, 'w+')
    file_obj_method_name_text_features=               open(method_name_text_features_db_file_path, 'w+')

   
    # 1. Write header in the file
    relation_name_if_expr_text_features =  project +"_if_expr_text_features"
    relation_name_till_if_log_level_text_features =  project +"_till_if_log_level_text_features"
    relation_name_operators_till_if_text_features =  project +"_operators_till_if_text_features"
    relation_name_variables_till_if_text_features =  project +"_variables_till_if_text_features"
    relation_name_method_call_names_till_if_text_features =  project +"_method_call_names_till_if_text_features"
    relation_name_method_param_type_text_features =  project +"_method_param_type_text_features"
    relation_name_method_param_name_text_features =  project +"_method_param_name_text_features"
    relation_name_package_name_text_features =  project +"_package_name_text_features"
    relation_name_class_name_text_features =  project +"_class_name_text_features"
    relation_name_method_name_text_features =  project +"_method_name_text_features"
   
   
    write_header_one_text_features(file_obj_if_expr_text_features, relation_name_if_expr_text_features, "if_expr_text_features")
    write_header_one_text_features(file_obj_till_if_log_level_text_features, relation_name_till_if_log_level_text_features, "till_if_log_level_text_features")
    write_header_one_text_features(file_obj_operators_till_if_text_features, relation_name_operators_till_if_text_features, "operators_till_if_text_features")
    write_header_one_text_features(file_obj_variables_till_if_text_features, relation_name_variables_till_if_text_features, "variables_till_if_text_features")
    write_header_one_text_features(file_obj_method_call_names_till_if_text_features, relation_name_method_call_names_till_if_text_features, "method_call_names_till_if_text_features")
    write_header_one_text_features(file_obj_method_param_type_text_features, relation_name_method_param_type_text_features, "method_param_type_text_features")
    write_header_one_text_features(file_obj_method_param_name_text_features, relation_name_method_param_name_text_features, "method_param_name_text_features")
    write_header_one_text_features(file_obj_package_name_text_features, relation_name_package_name_text_features, "package_name_text_features")
    write_header_one_text_features(file_obj_class_name_text_features, relation_name_class_name_text_features, "class_name_text_features")
    write_header_one_text_features(file_obj_method_name_text_features, relation_name_method_name_text_features, "method_name_text_features")
   
    #2. write database ibstabces
    for d in total_data:   
        write_in_file_bool_features(file_obj_if_expr, d)
    
    
    file_obj_if_expr.close()                   

#===================================================#
#  call- functions                                  #

#create_one_complete_all_features(all_features_db_file_path)
#create_one_complete_num_features(num_features_db_file_path)
#create_one_complete_bool_features(bool_features_db_file_path)
#create_one_complete_text_features(text_features_db_file_path)
create_one_complete_all_features_with_in_nb_bn_score(all_features_with_in_nb_bn_score_file_path)
create_one_complete_all_features_cross_nb_bn_score(all_features_cross_nb_bn_score_file_path)

"""
# Following functions is not running currently, will make in future
create_different_text_features_files(if_expr_text_features_db_file_path, till_if_log_level_text_features_db_file_path,  operators_till_if_text_features_db_file_path, variables_till_if_text_features_db_file_path, 
                            method_call_names_till_if_text_features_db_file_path, method_param_type_text_features_db_file_path, method_param_name_text_features_db_file_path,
                             package_name_text_features_db_file_path, class_name_text_features_db_file_path, method_name_text_features_db_file_path)

"""