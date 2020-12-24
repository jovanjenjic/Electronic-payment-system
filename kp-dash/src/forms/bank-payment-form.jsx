import React, { useRef } from 'react';
import Card from 'react-credit-cards';
import styled from 'styled-components';

import {
  formatCreditCardNumber,
  formatCVC,
  formatExpirationDate,
} from './utils';

import 'react-credit-cards/es/styles-compiled.css';

const Container = styled.div`
  a {
    color: #666;
  }

  h1 {
    font-weight: bold;
    margin: 0 0 10px;
  }

  h4 {
    margin-bottom: 30px;
  }

  h1,
  h2,
  h4 {
    text-align: center;
  }

  .App-payment {
    padding: 30px;
  }

  form {
    margin: 30px auto 0;
    max-width: 400px;
  }

  .form-actions {
    margin-top: 15px;
  }

  .App-cards {
    margin: 0 auto;
    max-width: 1280px;
    text-align: center;
  }

  .App-cards-list {
    display: flex;
    margin-top: 30px;
    flex-wrap: wrap;
    justify-content: space-between;
  }

  .App-cards-list > * {
    transform: scale(0.8);
    margin-bottom: 30px !important;
  }

  .App-highlight {
    font-family: serif;
    margin: 15px auto 0;
    max-width: 300px;
  }

  .App-highlight > div {
    padding-left: 40px;
  }

  .App-badges {
    align-items: center;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .App-badges a {
    display: block;
  }

  .App-badges a + a {
    margin-top: 18px;
  }
  .App-credits {
    background-color: #000;
    color: #fff;
    line-height: 50px;
    text-align: center;
  }

  .App-credits a {
    color: #fff;
    font-weight: bold;
  }

  @media screen and (min-width: 600px) {
    .App-badges {
      flex-direction: row;
    }

    .App-badges a + a {
      margin-top: 0;
      margin-left: 18px;
    }
  }

  small {
    font-size: 80%;
    font-weight: 400;
  }

  .form-control {
    display: block;
    width: 100%;
    height: calc(1.5em + 0.75rem + 2px);
    padding: 0.375rem 0.75rem;
    font-size: 1rem;
    font-weight: 400;
    line-height: 1.5;
    color: #495057;
    background-color: #fff;
    background-clip: padding-box;
    border: 1px solid #ced4da;
    border-radius: 0.25rem;
    transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
  }

  .row {
    display: flex;
    flex-direction: row;
    margin-top: 15px;
  }

  .col-6 {
    -ms-flex: 0 0 50%;
    flex: 0 0 50%;
    max-width: 50%;
  }

  .padding-left {
    padding-left: 10px;
  }

  .padding-right {
    padding-right: 10px;
  }

  .btn {
    display: inline-block;
    font-weight: 400;
    color: #212529;
    text-align: center;
    vertical-align: middle;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    background-color: transparent;
    border: 1px solid transparent;
    padding: 0.375rem 0.75rem;
    font-size: 1rem;
    line-height: 1.5;
    border-radius: 0.25rem;
    transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out,
      border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
  }

  .btn-block {
    display: block;
    width: 100%;
  }
  .btn-primary {
    color: #fff;
    background-color: #007bff;
    border-color: #007bff;
  }
`;

const BankPaymentForm = ({ onSubmit = () => {} }) => {
  const [fields, updateFields] = React.useState({
    number: '',
    name: '',
    expiry: '',
    cvc: '',
    issuer: '',
    focused: '',
  });

  const form = useRef(null);

  /** `handler` used for the input change */
  const handleInputChange = ({ target }) => {
    if (target.name === 'number') {
      target.value = formatCreditCardNumber(target.value);
    } else if (target.name === 'expiry') {
      target.value = formatExpirationDate(target.value);
    } else if (target.name === 'cvc') {
      target.value = formatCVC(target.value);
    }

    updateFields((prevState) => {
      return {
        ...prevState,
        [target.name]: target.value,
      };
    });
  };

  const handleInputFocus = ({ target }) => {
    updateFields({ ...fields, focused: target.name });
  };

  const handleCallback = ({ issuer }, isValid) => {
    if (isValid) {
      updateFields({ ...fields, issuer });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await onSubmit(fields);
    // case to reset form fields
    if (form && form.current) form.current.reset();
  };

  return (
    <Container>
      <div className="App-payment">
        <h1>Bank registration</h1>
        <h4>Populate credit card info</h4>
        <Card
          number={fields.number}
          name={fields.name}
          expiry={fields.expiry}
          cvc={fields.cvc}
          focused={fields.focused}
          callback={handleCallback}
        />
        <form ref={form} onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="tel"
              name="number"
              className="form-control"
              placeholder="Card Number"
              pattern="[\d| ]{16,22}"
              required
              onChange={handleInputChange}
              onFocus={handleInputFocus}
            />
            <small>E.g.: 49..., 51..., 36..., 37...</small>
          </div>
          <div className="form-group">
            <input
              type="text"
              name="name"
              className="form-control"
              placeholder="Name"
              required
              onChange={handleInputChange}
              onFocus={handleInputFocus}
            />
          </div>
          <div className="row">
            <div className="col-6 padding-right">
              <input
                type="tel"
                name="expiry"
                className="form-control"
                placeholder="Valid Thru"
                pattern="\d\d/\d\d"
                required
                onChange={handleInputChange}
                onFocus={handleInputFocus}
              />
            </div>
            <div className="col-6 padding-left">
              <input
                type="tel"
                name="cvc"
                className="form-control"
                placeholder="CVC"
                pattern="\d{3,4}"
                required
                onChange={handleInputChange}
                onFocus={handleInputFocus}
              />
            </div>
          </div>
          <input type="hidden" name="issuer" value={fields.issuer} />
          <div className="form-actions">
            <button className="btn btn-primary btn-block">Submit</button>
          </div>
        </form>
      </div>
    </Container>
  );
};

export default BankPaymentForm;
