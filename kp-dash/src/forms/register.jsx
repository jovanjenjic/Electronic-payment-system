import React from 'react';
import { Form, Input, Button } from 'antd';

import { FormContainer } from '../components/styledForm';

const layout = {
  labelCol: {
    span: 8,
  },
  wrapperCol: {
    span: 16,
  },
};

const Register = ({ onFinish = () => { }, form }) => {
  return (
    <FormContainer>
      <h2 className="title">Register</h2>
      <Form
        {...layout}
        name="basic"
        initialValues={{
          remember: true,
        }}
        onFinish={onFinish}
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
              type: 'email'
            },
          ]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="PIB"
          name="pib"
          rules={[
            {
              required: true,
              message: 'Please input your pib!',
            },
            {
              type: "number",
              transform: value => +value,
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
          Register
        </Button>
      </Form>
    </FormContainer>
  );
};

export default Register;