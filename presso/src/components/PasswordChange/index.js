import React, { Component } from 'react';
import { withFirebase } from '../Firebase';
import './index.css';
const INITIAL_STATE = {
  passwordOne: '',
  passwordTwo: '',
  error: null,
};
class PasswordChangeForm extends Component {
  constructor(props) {
    super(props);
    this.state = { ...INITIAL_STATE };
  }
  onSubmit = event => {
    const { passwordOne } = this.state;
    this.props.firebase
      .doPasswordUpdate(passwordOne)
      .then(() => {
        this.setState({ ...INITIAL_STATE });
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
    const { passwordOne, passwordTwo, error } = this.state;
    const isInvalid =
      passwordOne !== passwordTwo || passwordOne === '';
    return (
      <div>
      <form onSubmit={this.onSubmit}>
        <div className="form-group ">
                    <label>New Password</label>
        <input
          name="passwordOne" class="form-control"
          value={passwordOne}
          onChange={this.onChange}
          type="password"
          placeholder="New Password"
        /><br/></div>
        <div className="form-group ">
                    <label>Confirm new password</label>
        <input
          name="passwordTwo" class="form-control"
          value={passwordTwo}
          onChange={this.onChange}
          type="password"
          placeholder="Confirm New Password"
        /><br/></div>
        <button disabled={isInvalid} className="btn btn-primary btn-block" type="submit">
          Reset My Password
        </button><br/>
        {error && <p>{error.message}</p>}
      </form></div>
    );
  }
}
export default withFirebase(PasswordChangeForm);