import React from 'react';
import { Link } from 'react-router-dom';
import * as ROUTES from '../../constants/routes';

import { withFirebase } from '../Firebase';



const SignOutButton = ({ firebase }) => ( 
      <Link className="nav-link" onClick={firebase.doSignOut}  to={ROUTES.MAIN}>
        Sign Out
      </Link>  
);
export default withFirebase(SignOutButton);