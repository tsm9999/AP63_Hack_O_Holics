import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import { withFirebase } from '../Firebase';
import { PDFDownloadLink, Text, Document, Page } from '@react-pdf/renderer'
import ReactPDF from '@react-pdf/renderer';
import './index.css';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas'

const pdf = () => (

  <div id="pdfform">
    <PdfPage />    
  </div>

);

const INITIAL_STATE = {
  hospital: '',
  doctorname: '',
  phonenumber: '',
  degree: '',
  lic_no: '',

  username: '',
  age: 0,
  symptoms: '',
  diagnosis: '',
  prescription: '',
  suggestions: '',
  advice: '',
  error: null,
};

class pdfBase extends Component {
  constructor(props) {
    super(props);   
  console.log(this.props.location.state);
	this.state = { username: this.props.location.state.PatientName,
  age: this.props.location.state.Age,
  symptoms: this.props.location.state.Symptoms,
  diagnosis: this.props.location.state.Diagnosis,
  prescription: this.props.location.state.Medicines,
  suggestions: this.props.location.state.Recommendations,
  advice: this.props.location.state.Advice,
  error: null, };
  let db = this.props.firebase.db;
  this.props.firebase.auth.onAuthStateChanged(function(user) {
    if (user) {
      // User is signed in.
      
      db.settings({
        timestampsInSnapshots: true
      });
      db.collection("Doctor_Details")
      .doc(user.email)
      .get()
      .then(doc => {
        const data = doc.data();
        document.getElementById('hospital').innerHTML = data.hospital;
        document.getElementById('degree').innerHTML = "Degree: " + data.degree;
        document.getElementById('doctorname').innerHTML = "Doctor Name: " + data.fullname;
        document.getElementById('phonenumber').innerHTML = "Contact: " + data.phonenumber;
        document.getElementById('lic_no').innerHTML = "Licence no: " + data.lic_no;  
      
      }).catch(function(error) {
      console.log("Got an error: ",error);
      });
    } 

  
});
  
  }

  componentDidMount() {
    this.authSubscription = this.props.firebase.auth.onAuthStateChanged((user) => {
      this.setState({
        loading: false,
        user,
      });
    });
  }
  

  onSubmit = event => {
    event.preventDefault();
        //hide textarea    
    
    document.getElementById("sugg_txt").style.display = "none";
    document.getElementById("adv_txt").style.display = "none";
    document.getElementById("pre_txt").style.display = "none";
    document.getElementById("dia_txt").style.display = "none";
    document.getElementById("sym_txt").style.display = "none";
    document.getElementById("label_sugg").style.display = "none";
    document.getElementById("btn").style.display = "none";
    //set text
    document.getElementById('p_advice').innerHTML = document.getElementById('adv_txt').value;
    document.getElementById('p_pre').innerHTML = document.getElementById('pre_txt').value;
    document.getElementById('p_dia').innerHTML = document.getElementById('dia_txt').value;
    document.getElementById('p_sym').innerHTML = document.getElementById('sym_txt').value;


    const input = document.getElementById('pdfform');
    html2canvas(input, {
        scrollX: 0,
        scrollY: -window.scrollY
      })
      .then((canvas) => {
        const imgData = canvas.toDataURL('image/jpeg', 1.0);
        const ctx = canvas.getContext("2d");
        ctx.scale(0.8, 0.8);
        const pdf = new jsPDF("p", "mm", "a4");
        const imgProps= pdf.getImageProperties(imgData);
        pdf.addImage(imgData, 'JPEG', 10, 12, 190, 250);
        // pdf.output('dataurlnewwindow');
        pdf.save(this.props.location.state.PatientName +"_prescription.pdf");
      })
    ;
    document.getElementById('p_advice').innerHTML = "";
    document.getElementById('p_pre').innerHTML = "";
    document.getElementById('p_dia').innerHTML = "";
    document.getElementById('p_sym').innerHTML = "";

    document.getElementById("btn").style.display = "block";
    document.getElementById("label_sugg").style.display = "block";
    document.getElementById("sugg_txt").style.display = "block";
    document.getElementById("adv_txt").style.display = "block";
    document.getElementById("pre_txt").style.display = "block";
    document.getElementById("dia_txt").style.display = "block";
    document.getElementById("sym_txt").style.display ="block";
    
      //update patient history
    const db = this.props.firebase.db;
        db.settings({
          timestampsInSnapshots: true
        });
        const userRef = db.collection("Patient_Details").doc(this.props.firebase.auth.currentUser.uid)
          .collection("History").doc().set({
          suggestions:  document.getElementById('sugg_text').value,
          diagnosis: document.getElementById('p_dia').innerHTML,
          symptoms: document.getElementById('p_sym').innerHTML,
          advice: document.getElementById('p_advice').innerHTML,
          prescription: document.getElementById('p_pre').innerHTML,          
        });  

  }
  
  onChange = event => {
	  this.setState({ [event.target.name]: event.target.value });
  };

  render() {
    const { 
      username, 
      age, 
      symptoms,
      diagnosis,
      prescription,
      suggestions,
      advice,
      error,
     } = this.state;

         
     const isInvalid =     
     prescription === '' ||
     username === '';
  
    return (

      <div>
      <center>
      <h1 id="hospital">My Hospital</h1>
      <div className="row">
        <div id="doctorname" className="col4">
          Doctor Name: XYZ
        </div><br/>
        <div id="lic_no" className="col4">
          License No.: 123456
        </div>
        <div id="degree" className="col4">
          Degree: MBBS
        </div>
      </div>
      
  </center>
           <hr /> 
      <form onSubmit={this.onSubmit}>
        <div className="input-group ">
        <div className="form-group myrow">
                    <label>Patient Name</label>
	      <input
          name="username" className="form-control"
          value={username}
          onChange={this.onChange}
          type="text"
          placeholder="patient name"
        /></div>
        <div className="input-group-prepend">
        <span></span>
        </div>
        
        <div className="form-group myrow">
                    <label>Age (yrs)</label>
        <input
          name="age" className="form-control"
          value={age}
          onChange={this.onChange}
          type="number"
          placeholder="age"
        /></div>

        <div className="form-group myrow">
                    <label>Patient Contact</label>
	      <input
          name="contact" className="form-control"
          type="text"
          placeholder="Contact number"
        /></div>
        </div>
        <hr />
       
        <div className="form-group ">
                    <label>Symptoms</label>
        <textarea
          name="symptoms" className="form-control"
          value={symptoms} id="sym_txt"
          onChange={this.onChange}
          type="text"
          placeholder="symptoms"
        /></div>        
        <p id="p_sym" ></p>
        <div className="form-group ">
                    <label>Diagnosis</label>
        <textarea
          name="diagnosis" className="form-control"
          value={diagnosis} id="dia_txt"
          onChange={this.onChange}
          type="text"
          placeholder="diagnosis"
        /></div>
        <p id="p_dia" ></p>
        <div className="form-group ">
                    <label>prescription</label>
        <textarea
          name="prescription" className="form-control"
          value={prescription} id="pre_txt"
          onChange={this.onChange}
          type="text"
          rows="4"
          placeholder="prescription"
        /></div>
        <p id="p_pre" ></p>
        <div className="form-group ">
                    <label id='label_sugg'>suggestions</label>
        <textarea
          name="suggestions" className="form-control"
          value={suggestions} id="sugg_txt"
          onChange={this.onChange}
          type="text"
          rows="4"
          placeholder="suggestions"></textarea>         
        </div>
        <div className="form-group ">
                    <label>Other advices</label>
        <textarea
          name="advice" className="form-control"
          value={advice} id="adv_txt"
          onChange={this.onChange}
          type="text"          
          placeholder="advices"
        /></div>       
        <p id="p_advice" ></p>
        <br/>
        <button id="btn" className="btn btn-primary btn-block" type="submit">
          Confirm and Generate
        </button><br/>
        {error && <p>{error.message}</p>}	  
      </form>
      <hr />
      <center>
        <div className="row">
          <div id="phonenumber" className="col6">
            Contact No.: 123456
          </div>
          <div className="col6">
            Timing: 10:00am-11:00pm
          </div>
        </div>
      </center>
      <div>  
  </div>
  </div>  //parent div 
    );
  }
}


const PdfPage =withRouter(withFirebase(pdfBase));

export default pdf;

export { PdfPage };