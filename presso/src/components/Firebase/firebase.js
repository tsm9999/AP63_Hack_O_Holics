import app from 'firebase/app';
import 'firebase/auth';
import 'firebase/database';
import 'firebase/firestore';


const config = {
  apiKey: "AIzaSyASmipGcBhywx0YkexKGjUVckN3fxOsKsk",
  authDomain: "sih-20.firebaseapp.com",
  databaseURL: "https://sih-20.firebaseio.com",
  projectId: "sih-20",
  storageBucket: "sih-20.appspot.com",
  messagingSenderId: "391351332804",
  appId: "1:391351332804:web:93a2ac6acdf7b61da0fe91",
  measurementId: "G-TTHR88JSJ2"
};



class Firebase {
  constructor() {
    app.initializeApp(config);
    this.auth = app.auth();	
    this.db = app.firestore();
  }
  
  // *** Auth API ***
  doCreateUserWithEmailAndPassword = (email, password) =>
    this.auth.createUserWithEmailAndPassword(email, password);
	
  doSignInWithEmailAndPassword = (email, password) =>
    this.auth.signInWithEmailAndPassword(email, password);
	
  doSignOut = () => {
    this.auth.signOut();
    // const history = useHistory();
    // history.push("/home");
  }
  
  doPasswordReset = email => this.auth.sendPasswordResetEmail(email);
  
  doPasswordUpdate = password =>
    this.auth.currentUser.updatePassword(password);
	
	// *** User API ***
  user = uid => this.db.ref(`users/${uid}`);
  
  users = () => this.db.ref('users');
}
export default Firebase;