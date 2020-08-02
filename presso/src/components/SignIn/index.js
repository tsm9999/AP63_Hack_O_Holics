import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { compose } from 'recompose';
import { SignUpLink } from '../SignUp';

import { PasswordForgetLink } from '../PasswordForget';
import { withFirebase } from '../Firebase';
import * as ROUTES from '../../constants/routes';
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
const SignInPage = () => (
  <form id="signform">
  <div>
   
    <h1>SignIn</h1>
    <SignInForm />	
    <PasswordForgetLink />
    <SignUpLink />
  </div></form>
);
const INITIAL_STATE = {
  email: '',
  password: '',
  error: null,
};
class SignInFormBase extends Component {
  constructor(props) {
    super(props);
    this.state = { ...INITIAL_STATE };
  }
  onSubmit = event => {
    const { email, password } = this.state;
    this.props.firebase
      .doSignInWithEmailAndPassword(email, password)
      .then(() => {
        this.setState({ ...INITIAL_STATE });
        this.props.history.push(ROUTES.HOME);
      })
      .catch(error => {
        this.setState({ error });
      });
    event.preventDefault();
  };
  onChange = event => {
    this.setState({ [event.target.name]: event.target.value });
  };
  render() {
    const { email, password, error } = this.state;
    const isInvalid = password === '' || email === '';
    return (
      
      <form onSubmit={this.onSubmit} >
        <br/>
         <div className="form-group ">
                    <label>Email address</label>
        <input
          name="email" className="form-control"  value={email}
          onChange={this.onChange}
          type="text"
          placeholder="Email Address"
        /></div><br/>
         <div className="form-group">
                    <label>Password</label>
        <input
          name="password" className="form-control"
          value={password}
          onChange={this.onChange}
          type="password"
          placeholder="Password"
        /></div><br/>
        <button disabled={isInvalid}  className="btn btn-primary btn-block" type="submit">
          Sign In
        </button>
        <br/> 
        {error && <p>{error.message}</p>}
      </form>
    );
  }
}
const SignInLink = () => (
  <p>
    Already signed in? <Link to={ROUTES.SIGN_IN}>Sign In</Link>
  </p>
);
const SignInForm = compose(
  withRouter,
  withFirebase,
)(SignInFormBase);
export default SignInPage;
export { SignInForm ,SignInLink};