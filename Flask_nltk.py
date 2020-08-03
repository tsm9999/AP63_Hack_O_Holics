from flask import Flask, request, jsonify

app = Flask(__name__)

# people = [{'name': 'Alice', 'birth-year': 1986},
#           {'name': 'Bob', 'birth-year': 1985}]
@app.route('/',methods=['GET', 'POST'])
def handle_request():
	if request.method == 'POST':
		text1 = request.get_data()
		text = text1.decode("utf-8")
		print(text)

	import os
	import pandas as pd
	import numpy as np
	from difflib import get_close_matches

	df = pd.read_excel("/home/ritika/SIH2020/SIH Data/medicines.xlsx",sheet_name="Sheet1",header=None)
	df.head()

	df1 = pd.read_excel("/home/ritika/SIH2020/SIH Data/sym1.xlsx",sheet_name="Sheet1",header=None)
	df1.head()
	sym_list = df1[0].astype('str')
	sym_list = sym_list.values.tolist()


	medicine_list = df[0].astype('str')

	medicine_list = medicine_list.values.tolist()

	# test_case = "teli card 40mg tablet"
	# test_case = "soldier 250mg/125mg tablet"
	#test_case = "card per capsule"
	test_case = "kardepa capsule"
	# test_case = "a ride on her 200 mg tablet"
	# test_case = "clinical 10mg tablet"
	# test_case = "silne kem 5mg tablet"

	get_close_matches(test_case,medicine_list,n=5,cutoff=0.5)
	
	
	
	
	import nltk
	    # nltk.internals.config_java(r'C:\Program Files\Java\jdk-12\bin\java.exe')
	from nltk.tag import StanfordNERTagger
	from nltk.corpus import stopwords
	from nltk.tokenize import word_tokenize 
	stop_words = set(stopwords.words('english'))
	
	import json
	
	import sys 
	import os
	# nltk.internals.config_java("C:/Program Files/Java/jdk-12/bin/java.exe")
	from nltk.tag import StanfordNERTagger
	from nltk.corpus import stopwords
	from nltk.tokenize import word_tokenize
	stop_words = set(stopwords.words('english'))
	
	stop_words.add("for")
	stop_words.remove("before")
	stop_words.remove("after")
	stop_words.remove("during")
	print(stop_words)
	
	#text='''Take Ridoherp EX Tablet for 17 days take Cardepa Capsule before dinner take Urotel 4 mg Capsule before lunch and after dinner for 2 days.'''
	#text='''take traxol O 200 mg tablet for two days before lunch take the correct 500mg injection before lunch for 3 days take'''
	
	lst = nltk.word_tokenize(text)
	tokens = [x.capitalize() for x in lst]
	entity_tagger=StanfordNERTagger('/home/ritika/SIH2020/stanford-ner-tagger/v7_ner_model.ser.gz', '/home/ritika/SIH2020/stanford-ner-tagger/stanford-ner-3.9.2.jar')
	#print(tokens)
	filtered_sentence=[]
	for w in tokens:
	    if w.lower() not in stop_words: 
	        filtered_sentence.append(w) 
	
	#print(filtered_sentence)
	tagged_entity = entity_tagger.tag(filtered_sentence)
	print(tagged_entity)
	search_med=""
	
	rngtuple=[None] * 10
	timtuple=[None] * 10
	medtuple=[None] * 10
	sym=[]
	index=-1
	
	i=0
	while i<len(tagged_entity):
	  pair=tagged_entity[i]
	  #print(pair)
	  #medicines
	  
	  #sym
	  if pair[1] == 'SYM':
	    print(pair)
	    i=i+1
	    if i >len(tagged_entity):
	      break
	    pair=tagged_entity[i]
	    sym_search=''
	    while pair[1]!='TIM' and pair[1]!='DAY' and pair[1]!= "T" and pair[1]!='SYM':
	      sym_search+=' '+pair[0]
	      i=i+1
	      if i >len(tagged_entity)-1:
	        break
	      pair=tagged_entity[i]
	    sym.append(get_close_matches(sym_search,sym_list,n=5,cutoff=0.4))
	    print(sym)
	    if pair[1]=='SYM':
	      continue
	      
	      
	  if pair[1]=='T':
	    i=i+1
	    if i >len(tagged_entity)-1:
	      break
	    pair=tagged_entity[i]
	    search_med=""
	    while pair[1]!='TIM' and pair[1]!='DAY' and pair[1]!= "T":
	      if pair[1]=='TYP':
	        index=index+1
	        search_med+=pair[0]+' '
	        print(search_med)
	        medtuple[index]= get_close_matches(search_med,medicine_list,n=5,cutoff=0.4)
	        i=i+1
	        if i >len(tagged_entity)-1:
	          break
	        pair=tagged_entity[i]
	        search_med=''
	      
	      search_med+=pair[0]+' '
	      i=i+1
	      if i >len(tagged_entity)-1:
	        break
	      pair=tagged_entity[i]
	      #print('--',pair)
	    if pair[1]=='TIM':
	      i=i-1
	    continue
	
	  #Time
	  if pair[1] == 'TIM':
			#timtuple[index]=' '+pair[0]
	    tim=' '+pair[0]
	    i=i+1
	    if i >len(tagged_entity)-1:
	      break
	    pair=tagged_entity[i]
	    while pair[1]!= 'T' and pair[1]!= 'DAY':
				#timtuple[index]+=' ' +pair[0]
	      tim+=' '+pair[0]
	      i=i+1
	      if i >len(tagged_entity)-1:
	        break
	      pair=tagged_entity[i]
	    #i=i-1
	    print(tim)
	    timtuple[index]=tim
			#prescription.append(('TIM',timtuple))
	    continue
	  
	  #day
	  if pair[1] == 'DAY' :
			#print(pair)
			#rngtuple=' '
	    i=i-1
	    if i >len(tagged_entity)-1:
	      break
	    pair=tagged_entity[i]
			#rngtuple[index]=' ' +pair[0]+' days'
	    rng=' '+pair[0]+' days'
	    print(rng)
	    rngtuple[index]=rng
	  i=i+1
			#prescription.append(('RNG',rngtuple))
	    
	  i=i+1
	
	print(medtuple)  
	print(timtuple)
	print(rngtuple)
	
	import json
	Medicines=[]
	Dose=[]
	Days=[]
	Symptoms=["Cough","Weaknes","Allergy","Soar Throat","cold"]
	Symptoms=sym
	print()
	for i in range(0,index+1):
	  if timtuple[i]==None:
	    if timtuple[i+1]!=None:
	      timtuple[i]=timtuple[i+1]
	    else:
	      timtuple[i]=timtuple[i-1]
	  elif rngtuple[i]==None:
	    if rngtuple[i+1]!=None:
	      rngtuple[i]=rngtuple[i+1]
	    else:
	      rngtuple[i]=rngtuple[i-1]
	  Medicines.append(medtuple[i])
	  Dose.append(timtuple[i])
	  Days.append(rngtuple[i])
	
	data_json = {}
	data_json["Medicines"] = Medicines
	data_json["Dose"] = Dose
	data_json["Days"] = Days
	data_json["Symptoms"] = Symptoms
	
	print(json.dumps(data_json))
	# with open("json_data.json", "w") as write_file:
	#     json.dump(data_json, write_file, indent = 4)
	
	
	
	
	return jsonify(data_json)

app.run(host="0.0.0.0", port=5005)
