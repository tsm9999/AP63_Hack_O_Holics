import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { withFirebase } from '../Firebase';
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import * as ROUTES from '../../constants/routes';
import ReactSearchBox from 'react-search-box'

const Homepage = () => (
  <div>
		<HomePageBase/>
  </div>
  
);

const INITIAL_STATE = {
  name: '',
  rid: '',  
  error: null,
  users: [],
 loading: false,
};

class HomePageBase extends Component{

  constructor(props) {
    super(props);	
    this.state = { ...INITIAL_STATE };
    let user = this.props.firebase.auth;
    let db = this.props.firebase.db;
    db.collection('Patient_Details').on('value', snapshot => {
      const usersObject = snapshot.val();
  const usersList = Object.keys(usersObject).map(key => ({
      ...usersObject[key],
      uid: key,
    }));	  
    this.setState({
      users: usersList,
      loading: false,
    });
  });       
  }
  
 
  render(){
    const {
      users,
    } = this.state;
    return (
<div>
  <div class="wrap">
   <div class="search">
     <center><h3>Search a patient</h3></center>
   <ReactSearchBox 
      placeholder="Enter patient name"
      data={users}
      onSelect={event =>  window.location.href=ROUTES.SPEECH}
      onFocus={() => {
        console.log('This function is called when is focussed')
      }}

      onChange={name => console.log(name)}
      fuseConfigs={{
        threshold: 0.05,
      }}
      value=" "
    />
</div>
 <br/>
    <h4>Or</h4>
    <br/>
    
    <h3> <Link to={ROUTES.PATIENT_REG}>Register new patient</Link></h3>
  </div>

 
  </div>
    );
  }
}

const HomePage =withRouter(withFirebase(HomePageBase));

export default Homepage;

export { HomePage };