import MySQLdb
import numpy as np

import utill6


"""=====================================================================================================
@ Author: Sangeeta
@Uses:
1. This file will be used to create dataset from the main training table "project_Training6.java
2. It will create 11 ARFF Files

    a. One having all the instances present in the main table
======================================================================================================"""

#Project
#"""
project= "tomcat"
title = 'Tomcat'
#"""
"""
project =  "cloudstack"
title = 'CloudStack'
#"""

"""
project =  "hd"
title = 'Hadoop'
#"""

"""
port=3306
user="root"
password="1234"
database="logging6_crossif"
main_source_table = project+"_if_training6_crossif"  # from this table we have to take the data
path = "F:\\Research\\L6-CROSS-IF\\dataset\\"
text_features_db_file_path = path+ project+"-arff\\if\\"+project+"_if_text_features_WITH_ID.arff"
"""

"""

port=3307
user="sangeetal"
password="sangeetal"
database="logging6_crossif"
main_source_table = project+"_if_training6_crossif"  # from this table we have to take the data
path = "E:\\Sangeeta\\Research\\L6-CROSS-IF\\dataset\\"
text_features_db_file_path = path+ project+"-arff\\if\\"+project+"_if_text_features_WITH_ID.arff"
#"""

## Feb 9 for JIIT SERVER ##

"""
port=3306
user="root"
password="1234"
database="logging6_crossif_rc_feb9"
main_source_table = project+"_if_training6_crossif"  # from this table we have to take the data
path = "F:\\Research\\L6-CROSS-IF\\dataset\\"
text_features_db_file_path = path+ project+"-arff\\if\\"+project+"_if_text_features_WITH_ID.arff"
"""

#"""
port=3306
user="root"
password="1234"
database="logging6_crossif_rc_feb9"
main_source_table = project+"_if_training6_crossif"  # from this table we have to take the data
path = "D:\\Sangeeta\\Research\\L6-CROSS-IF\\dataset\\"
text_features_db_file_path = path+ project+"-arff\\if\\"+project+"_if_text_features_WITH_ID.arff"
#"""

db1= MySQLdb.connect(host="localhost",user=user, passwd=password, db=database, port=port)
select_cursor = db1.cursor()
insert_cursor = db1.cursor()

  
#=======================================================#
#  @Uses:Write_header() is a function that is used to insert
# the ARFF header in the file.
#=======================================================#
def write_header_text_features(file_obj,relation_name):
    
    file_obj.write("@relation    "  + relation_name+"\n" )
    file_obj.write("@attribute is_if_logged {0,1}  "+"\n")   
    file_obj.write("@attribute  ifid  numeric "+"\n")
    file_obj.write("@attribute all_text_features_cleaned string "+"\n")
        
    file_obj.write("\n")
    file_obj.write("@data " +"\n")
      

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
    
    if_id  = tuple_val[29] # //@ NEW
    
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
    write_str =""+ (str)(is_if_logged )+","+ (str)(if_id)+",'"+   text_features+"' "
      
    # ==write in the file======#  
    file_obj.write(write_str+"\n")       
            
    #target.append(0)  Removing from here moving up                  
    #db1.commit()           

#=======================#
#  all text features  #
#======================#
def create_one_complete_text_features(text_features_db_file_path):
    #===========Read all the if blocks===============================#
   
   

    str_total_data = "select  if_expr, loc_till_if, is_till_if_logged, till_if_log_count, till_if_log_levels, operators_till_if, operators_count_till_if, variables_till_if,  \
                       variables_count_till_if,method_call_names_till_if, method_call_count_till_if,  is_return_in_till_if, throw_throws_till_if, \
                       if_in_till_if, if_count_in_till_if, is_assert_till_if, is_method_have_param,  method_param_type, method_param_name, method_param_count,\
                       is_return_in_if, throw_throws_if, is_assert_if, is_null_condition_if, is_instance_of_condition_if, package_name, class_name, method_name, is_if_logged, if_id\
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
    relation_name =  project +"_if_text_features_with_id"
    write_header_text_features(file_obj, relation_name)
    
    #2. write database ibstabces
    for d in total_data:   
        write_in_file_text_features(file_obj, d)
    
    
    file_obj.close()
  

#===================================================#
#  call- functions                                  #

create_one_complete_text_features(text_features_db_file_path)

