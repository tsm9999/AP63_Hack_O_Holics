import React from 'react';

class Landing extends React.Component {
  constructor(props){
    super(props);
    this.state = {apiResponse: ""};
  }

   callAPI(){
    fetch('http://localhost:9000/name?data="take crocin before lunch."')
    .then(res => res.text())
    .then(res => this.setState({apiResponse : res}));
  }

  componentWillMount() {
    this.callAPI();
  }
  render() {
    return (
      <div className = "Landing">
        <header className="Landing-Header"></header>
        
        <p>{this.state.apiResponse}</p>
      </div>
        
    );
  }

};

export default Landing;
