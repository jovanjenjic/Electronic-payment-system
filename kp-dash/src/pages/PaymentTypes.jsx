import React from 'react';
import styled from 'styled-components';
import { Select, Result, Form, Button, Input } from 'antd';

import { ShopContext } from '../context/shop';
import { FormContainer } from '../components/styledForm';

import useFetchPaymentForms from '../hooks/useFetchPaymentForms';

const Container = styled.div`
  display: flex;
  flex-direction: column;

  padding: 20px;
`;

const InfoContainer = styled.div`
  margin-top: 20px;
`;

const layout = {
  labelCol: {
    span: 8,
  },
  wrapperCol: {
    span: 16,
  },
};

const PaymentTypes = () => {
  const [form] = Form.useForm();

  const [value, updatevalue] = React.useState(null);

  const { paymentTypes } = React.useContext(ShopContext);

  const { data: formFields } = useFetchPaymentForms();

  const paymentTypesNames = paymentTypes.map((v) => v.type);

  const onFinish = () => {};

  return (
    <Container>
      <Select
        placeholder="Choose payment type"
        value={value}
        onChange={(value) => updatevalue(value)}
      >
        {['PAYPAL', 'BITCOIN', 'BANK'].map((v) => (
          <Select.Option key={v} value={v}>{`${v[0]}${v
            .slice(1)
            .toLowerCase()}`}</Select.Option>
        ))}
      </Select>
      <InfoContainer>
        {paymentTypesNames.includes(value) && (
          <Result
            title={`Payment credentials for ${value} already exists`}
            status="success"
          />
        )}
        {!paymentTypesNames.includes(value) && value && (
          <FormContainer>
            <h2 className="title">{value}</h2>
            <Form {...layout} name="basic" onFinish={onFinish} form={form}>
              {Object.keys(formFields[value] || {}).map((key) => (
                <Form.Item key={key} label={key} name={key}>
                  <Input />
                </Form.Item>
              ))}
              {/* <Form.Item
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
              </Form.Item> */}

              <Button
                type="primary"
                block
                htmlType="submit"
                className="form-button"
              >
                Add payment type
              </Button>
            </Form>
          </FormContainer>
        )}
      </InfoContainer>
    </Container>
  );
};

export default PaymentTypes;
