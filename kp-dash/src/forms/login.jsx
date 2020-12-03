import React from 'react';
import { Form, Input, Button } from 'antd';
import { Link } from 'react-router-dom';

import { FormContainer } from '../components/styledForm';

const layout = {
  labelCol: {
    span: 8,
  },
  wrapperCol: {
    span: 16,
  },
};

const Login = ({ onFinish = () => { }, form }) => {
  return (
    <FormContainer>
      <h2 className="title">Login</h2>
      <Form
        {...layout}
        name="basic"
        initialValues={{
          remember: true,
        }}
        onFinish={onFinish}
        form={form}
      >
        <Form.Item
          label="Email"
          name="email"
          rules={[
            {
              required: true,
              message: 'Please input your email!',
            },
            {
              type: 'email',
            },
          ]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Password"
          name="password"
          rules={[
            {
              required: true,
              message: 'Please input your password!',
            },
          ]}
        >
          <Input.Password />
        </Form.Item>

        <Button type="primary" block htmlType="submit" className="form-button">
          Login
        </Button>
        <div className="register-text">If you not have an account</div>
        <Link to="/register"><div className="register">Register now!</div></Link>
      </Form>
    </FormContainer>
  );
};

export default Login;