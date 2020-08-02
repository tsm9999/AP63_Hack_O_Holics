import React, { Component } from 'react';
import {
  BrowserRouter as Router,
  Route,
} from 'react-router-dom';

import pdf from '../Pdf';
import SignUpPage from '../SignUp';
import SignInPage from '../SignIn';
import PasswordForgetPage from '../PasswordForget';
import HomePage from '../Home';
import AccountPage from '../Account';
import AdminPage from '../Admin';
import * as ROUTES from '../../constants/routes';
import { withAuthentication } from '../Session';
import Speech from '../Speech';
import Scheduling from '../Scheduling';
import Assistant from '../Assistant'
import Navigation from '../Navigation';
import Patientreg from '../Patientreg';
import Main from '../Main';
const App = () => (
  <Router>
    <div>
      <Navigation />
      <Route exact path={ROUTES.LANDING} component={Main} />
      <Route path={ROUTES.MAIN} component={Main} />
      <Route path={ROUTES.SIGN_UP} component={SignUpPage} />
      <Route path={ROUTES.SIGN_IN} component={SignInPage} />
      <Route
        path={ROUTES.PASSWORD_FORGET}
        component={PasswordForgetPage}
      />
     
      <Route path={ROUTES.HOME} component={HomePage} />
      <Route path={ROUTES.SPEECH} component={Speech} />
      <Route path={ROUTES.PRESCRIPTION} component={pdf} />
      <Route path={ROUTES.SCHEDULING} component={Scheduling} />
      <Route path={ROUTES.ASSISTANT} component={Assistant} />
      <Route path={ROUTES.ACCOUNT} component={AccountPage} />
      <Route path={ROUTES.ADMIN} component={AdminPage} />
      <Route path={ROUTES.PATIENT_REG} component={Patientreg} />
    </div>
  </Router>
);
export default withAuthentication(App);