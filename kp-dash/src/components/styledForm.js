import styled from 'styled-components';

export const Container = styled.div`
  background-color: #a9c9ff;
  background-image: linear-gradient(180deg, #a9c9ff 0%, #ffbbec 100%);
  width: 100%;
  height: ${window.innerHeight}px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const FormContainer = styled.div`
  border: 1px sold #888888;
  border-radius: 15px;
  padding: 20px;
  background: #ffffff;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;

  .form-button {
    margin-top: 5px;
    border-radius: 8px;
  }

  .register-text {
    color: #888888;
    margin-top: 12px;
  }

  .title {
    text-align: center;
    margin-bottom: 20px;
  }

  .register {
    font-size: 12px;
  }
`;
