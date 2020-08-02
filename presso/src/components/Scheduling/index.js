import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { withFirebase } from '../Firebase';
import firebase from 'firebase';

import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import * as ROUTES from '../../constants/routes';
import ReactSearchBox from 'react-search-box'
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/dist/styles/ag-grid.css';
import 'ag-grid-community/dist/styles/ag-theme-alpine.css';

class scheduling extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      users: [],
      db: firebase.firestore(),

      columnDefs: [{
        headerName: "Patient ID", field: "id", sortable: true, filter: true
        },{
        headerName: "Patient Name", field: "name", sortable: true, filter: true
        },{
          headerName: "Age", field: "age", sortable: true, filter: true
        }, 
    ],
        rowData: [
          { id: "Toyota", name: "Celica", age: 35 },
          { id: "Ford", name: "Mondeo", age: 32 },
          { id: "Porsche", name: "Boxter", age: 72 }]
      
    };
    firebase.firestore().settings({ timestampsInSnapshots: true});

  }

   async getData() {
     let tempDoc = [];
    await this.props.firebase.db.collection('Patient_Details').get().then((querySnapshot) => {
     // const tempDoc = []
      querySnapshot.forEach((doc) => {
         tempDoc.push({ key: doc.id, value:doc.data().name, ...doc.data() })
      })
      console.log(tempDoc);
      this.setState({
        users: tempDoc,
        loading: false,
      });          
   });
   return tempDoc;
  }

  async componentDidMount() {
    this.setState({ loading: true });
    
    await this.props.firebase.db.collection('Patient_Details').get().then((querySnapshot) => {
      const tempDoc = []
      querySnapshot.forEach((doc) => {
         tempDoc.push({ id: doc.id, value:doc.data().name, ...doc.data() })
      })
      console.log(tempDoc);
      this.setState({
        users: tempDoc,
        loading: false,
      });       
   });
	  
  }
  
  componentWillUnmount() {
   // firebase.firestore().collection('patients');
  }  
  
  render() {
    const { users, loading } = this.state;
    //const u = this.getData();
    console.log(users);
    return (
      <div>
      
  <div class="wrap">
   <div class="search">
     <center><h3>Search a patient</h3></center>
     
    <h3> <Link to={ROUTES.PATIENT_REG}>Register new patient</Link></h3>
  </div>
  <br/>
    <br/>
  
<div className="ag-theme-alpine" style={ {height: '200px', width: '600px'} }>
        <AgGridReact
            columnDefs={this.state.columnDefs}
            rowData={this.state.users}
            onGridReady={ params => console.log("API", params.api) }  
            >
        </AgGridReact>
      </div>
</div>
  
  {/* <UserList users={users} />     */}
  </div>


);
  }
}
//to check
// const UserList = ({ users }) => (
//   <ul>
//     {users.length>0 && users.map(user => (
//       <li key={user.key}>
//         <span>
//           <strong>Email:</strong> {user.email}
//         </span>
        
//         <span>
//           <strong>Name:</strong> {user.name}
//         </span>
//       </li>
//     ))}
//   </ul>
// );

export default withFirebase(scheduling);