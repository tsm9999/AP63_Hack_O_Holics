
import React, { Component } from 'react';

import { withRouter } from 'react-router-dom';

import { withFirebase } from '../Firebase';
import 'bootstrap/dist/css/bootstrap.min.css';

import './index.css';

import firebase from 'firebase';

const SchedulePage = () => (
  <div id="psignupform">
    <h1>Patient Registration</h1>
		<PATSignUpForm/>
  </div>
  
);


const INITIAL_STATE = {  
  name: '',
  email: '',
  age: '',
  gender: '',  
  error: null,
  users: [],
 loading: false,
};

class AppointmentFormBase extends Component {
  constructor(props) {
    super(props);
	
	this.state = { ...INITIAL_STATE };
  }
  
  
  

  addUser = e => {
    e.preventDefault();
    const db = firebase.firestore();
    db.settings({
      timestampsInSnapshots: true
    });
    const userRef = db.collection("Patient_Details").add({
      name: this.state.name,
      email: this.state.email,
      age: this.state.age,
      gender: this.state.gender
    });  
    this.setState({
      name: "",
      email: "",
      age: "",
      gender: "",

    });
  };
  onChange = event => {
	  this.setState({ [event.target.name]: event.target.value });
  };
  render() {
	  const {
      name,
      email,
      age,
      gender,
      error,
      users,
      loading,
    } = this.state;
	
	const isInvalid = age === '' || name === '' || gender === '' || email === '';
	  
    return (
      <div>
        <div className="addForm">
            <form onSubmit={this.addUser}>
        <div className="form-group ">
                    <label>Full Name</label>
	  <input
          name="name" className="form-control"
          value={name}
          onChange={this.onChange}
          type="text"
          placeholder="Full Name"
        /></div>
        <div className="form-group ">
                    <label>Email address</label>

        <input
          name="email" className="form-control"
          value={email}
          onChange={this.onChange}
          type="text"
          placeholder="Email Address"
        /></div>
        <div className="form-group ">
                    <label>Age</label>
        <input
          name="age" className="form-control"
          value={age}
          onChange={this.onChange}
          type="number"
          placeholder="Age"
        /></div>
        <div className="form-group ">
                    <label>Gender</label>
        <input
          name="gender" className="form-control"
          value={gender}
          onChange={this.onChange}
          type="text"
          placeholder="Gender"
        /></div><br/>
              <button disabled={isInvalid} className="btn btn-primary btn-block" type="submit">
                Register
              </button>
              {error && <p>{error.message}</p>}	  
            </form>
        </div>
        
    </div>
    );
  }
}

const PATSignUpForm =withRouter(withFirebase(AppointmentFormBase));

export default SchedulePage;

export { PATSignUpForm };