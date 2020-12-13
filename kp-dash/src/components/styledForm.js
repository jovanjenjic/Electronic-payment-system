import styled from 'styled-components';

export const Container = styled.div`
  background: rgb(2, 0, 36);
  background: linear-gradient(
    90deg,
    rgba(2, 0, 36, 1) 0%,
    rgba(9, 9, 121, 0.7570378493194152) 31%,
    rgba(0, 212, 255, 1) 100%
  );
  width: 100%;
  height: ${window.innerHeight}px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const FormContainer = styled.div`
  border-radius: 15px;
  padding: 20px;
  background: #ffffff;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  transition: all 0.2s ease-in-out;

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

  &:hover {
    cursor: pointer;
    transform: scale(1.05);
  }
`;
