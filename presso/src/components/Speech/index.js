import React, { Component } from 'react';
import * as ROUTES from '../../constants/routes';
import './index.css';
import { Link, withRouter } from 'react-router-dom';
import { withFirebase } from '../Firebase';
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine.css';
const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition 
const recognition = new SpeechRecognition()

recognition.continous = true
recognition.interimResults = true
recognition.lang = 'en'

var apiKey = "AIzaSyASmipGcBhywx0YkexKGjUVckN3fxOsKsk";
var options = {
  concurrentLimit: 20,
  requestOptions: {
    proxy: 'http://123.123.123.123:8080',   
  },
};
var googleTranslate = require('google-translate')(apiKey, options);

class Speech extends React.Component {

  constructor(props) {
    super(props);
   

    this.state = {
      listening: false,
      users: [],
      history: this.props.location.state.history,
      key: this.props.location.state.key,
      name: this.props.location.state.name,
      age: this.props.location.state.age,
      gender: this.props.location.state.gender,
      contact: this.props.location.state.contact,

      columnDefs: [{
        headerName: "Date", field: "date",  width: 100, sortable: true, filter: true, resizable: true,
        },{
          headerName: "Symptoms", field: "symptoms",  width: 150, resizable: true, sortable: true, filter: true 
        },{
        headerName: "Diagnosis", field: "diagnosis",  width: 150, resizable: true, sortable: true, filter: true
        },{
          headerName: "Prescription", field: "prescription",  width: 350, resizable: true, sortable: true, filter: true
        }, {
          headerName: "Advices", field: "advice",  width: 150, resizable: true, sortable: true, filter: true
        }, 
    ],
        // rowData: [
        //   { date: "20/7/2020", sym: "Cough", dia: "Throat infection", med: "Crocin 250mg after lunch after dinner for 2 days", adv: "drink warm water"},
        //   { date: "23/6/2019", sym: "body pain", dia: "df", med: "Crocin 250mg after lunch after dinner for 2 days", adv: "drink warm water"},
        //   { date: "1/8/2020", sym: "Cough", dia: "Corona", med: "Crocin 250mg after lunch after dinner for 2 days", adv: "drink warm water"},
        // ]
    }

    console.log('sp his',this.state.history);   

    this.i=0;
    this.text=[];
    this.sugg_text='';
    this.sym_text='';
    this.adv_text='';
    this.dia_text='';
    this.toggleListen = this.toggleListen.bind(this);
    this.handleListen = this.handleListen.bind(this);
    this.callAPI = this.callAPI.bind(this);


  }
  
  callAPI(){

    fetch('http://localhost:9000/name?data='+ document.getElementById('finaltext').value)
    .then(res => res.json())
    .then(res => {
      this.setState({apiResponse : res})
  
      if(res.Dose!=null)
      for (this.i = 0; this.i < res.Dose.length; this.i++) {
        this.text.push(" for "+res.Days[this.i]+" Days, "+res.Dose[this.i]+"\n");
      }

      if(res.Symptom!=null)
      for (this.i = 0; this.i < res.Symptom.length; this.i++) {
        this.sym_text += res.Symptom[this.i]+"\n";
      }

      if(res.Advice!=null)
      for (this.i = 0; this.i < res.Advice.length; this.i++) {
        this.adv_text += res.Advice[this.i]+"\n";
      }

      if(res.Recommendations!=null)
      for (this.i = 0; this.i < res.Recommendations.length; this.i++) {
        this.sugg_text += res.Recommendations[this.i]+"\n";
      }

      if(res.Diagnosis!=null)
      for (this.i = 0; this.i < res.Diagnosis.length; this.i++) {
        this.dia_text += res.Diagnosis[this.i]+"\n";
      }
     
      //
      this.props.history.push({
        pathname: ROUTES.PRESCRIPTION,
        state : {
        Key: this.state.key,
        PatientName: res.PatientName,
        Age: res.Age,
        Symptoms: this.sym_text,
        Diagnosis: this.dia_text,
        Medicines: res.Medicines,
        Dose: this.text,
        Advice: this.adv_text,
        Recommendations: this.sugg_text,
        }
      }) 
  }); 
  }

  toggleListen() {
    this.state.listening = !this.state.listening;
     this.handleListen();
  }
  
  handleListen(){ 
    if (this.state.listening) {
      document.getElementById('microphone-btn').innerHTML = "stop";
      recognition.start()
    } else {
      document.getElementById('microphone-btn').innerHTML = "record";
      recognition.stop()      
    }

    let finalTranscript = ''
    recognition.onresult = event => {
      let interimTranscript = ''

      for (let i = event.resultIndex; i < event.results.length; i++) {
        const transcript = event.results[i][0].transcript;
        if (event.results[i].isFinal) finalTranscript += transcript + ' ';
        else interimTranscript += transcript;
      }
      if(interimTranscript.length<=1){
        //document.getElementById('interim').innerHTML = "!"  
      }else{
        //document.getElementById('interim').innerHTML = interimTranscript
      }

      document.getElementById('finaltext').value = finalTranscript;

      // googleTranslate.detectLanguage(finalTranscript, function(err, detection){
      //   console.log(detection);
      //   // =>  { language: "en", isReliable: false, confidence: 0.5714286, originalText: "Hello" }
      // });

      // googleTranslate.translate(finalTranscript, 'en', function(err, translation) {
      //   console.log(translation.translatedText);
      //   // =>  Mi nombre es Brandon
      // });
      googleTranslate.translate(finalTranscript, 'en', function(err, translation) {
        console.log(translation);
        // =>  { translatedText: 'Hallo', originalText: 'Hello', detectedSourceLanguage: 'en' }
      });
      
    } 
}



async componentDidMount() {
//   this.setState({ loading: true });
  
//   await this.props.firebase.db.collection('Patient_Details')
//                           .doc(this.state.key)
//                           .collection('History')
//                           .get().then((querySnapshot) => {
//     const tempDoc = []
//     querySnapshot.forEach((doc) => {
//        tempDoc.push({ id: doc.id, value:doc.data().name, ...doc.data() })
//     })
//     console.log(tempDoc);
//     this.setState({
//       users: tempDoc,
//       loading: false,
//     });       
//  });
  
}

onSelectionChanged() {
  var selectedRows = this.api.getSelectedRows();
  console.log('row', selectedRows[0].date)
  // document.querySelector('#selectedRows').innerHTML =
  //   selectedRows.length === 1 ? selectedRows[0].athlete : '';
}

  render() {
    const { 
      name, 
      age, 
      gender,
      contact,
     } = this.state;
    return (
      
      <div>
        
        <div className="split left">
		<div className="back">
		{/* <div className="centered">
			<img src="avatar.jpeg" alt="Avatar"></img>
		</div> */}
		
			<h4 className="mgbt-xs-15"><center>Patient Profile</center></h4>
			
      <table>
							<tr id="p_details">
								<td><p>Name: </p></td>
								<td><p id="p_name">{name}</p></td>
								<td><p>Contact: </p></td>
                <td><p id="p_contact">{contact}</p></td>
							</tr>
							<tr>
								<td><p>Gender: </p></td>
								<td><p id="p_gender">{gender}</p></td>
								<td rowSpan="2"><p>Allegies:</p></td>
								<td rowSpan="2"><p id="p_allergy">No allergies</p></td>
							</tr>
							<tr>
								<td><p>Age: </p></td>
								<td><p id="p_age">{age}</p></td>
							</tr>
						</table>
            

            <div id="heading"> 
            <h4 className="mgbt-xs-15" ><center>Previous Prescriptions</center></h4>
            </div>
						
<div className="ag-theme-alpine" style={ {width: '100%'} }>
        <AgGridReact
            columnDefs={this.state.columnDefs}
            rowData={this.state.history}
            domLayout={'autoHeight'}
            rowSelection= {'single'}
            animateRows={true}
            onSelectionChanged={ this.onSelectionChanged}            
            onGridReady={ params => {console.log("API", params.api); params.api.resetRowHeights();} }  
            >
        </AgGridReact>
      </div>
					
		</div>
	</div>

  <div className="split right">
    <div className="back">		
    <div>
          <button id="microphone-btn" className="btn btn-primary btn-block btn-lg" onClick={this.toggleListen}>Record</button>
        
          <button id="recognize-btn" className="btn btn-primary btn-block btn-lg" onClick={this.callAPI} type="submit">PDF</button>
       
    </div>
      <div id="interim" color='gray'></div>
      
        
        <textarea id="finaltext" className="form-control txt" rows="10" placeholder="transcript" color='black'/>
       
		</div>
	  </div>
    </div>
    )
  }
}

export default Speech;

const speechPage =withRouter(withFirebase(Speech));

export { speechPage };
