import React from 'react';
import styled from 'styled-components';

import { Card, Button, Modal, Form, Input, notification } from 'antd';

import { ShopContext } from '../context/shop';

import useFetchItems from '../hooks/useFetchItems';
import { post } from '../services/api';
import { ADD_ITEM_URL } from '../constants/url';
import { responseOk } from '../utils/responseOk';

const { Meta } = Card;

const Container = styled.div`
  display: grid;
  grid-template-columns: 20% 20% 20% 20% 20%;
  padding: 20px;

  @media only screen and (max-width: 600px) {
    display: grid;
    grid-template-columns: 50% 50%;
  }
`;

const CardWrapper = styled.div`
  margin: 10px;
  transition: all 0.2s ease-in-out;

  &:hover {
    cursor: pointer;
    box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
    transform: scale(1.05);
  }
`;

const Description = styled.div`
  display: flex;
  flex-direction: column;

  p {
    margin-bottom: 0px;
  }

  &.price {
    font-size: 13px;
    font-family: 'Arimo';
    font-weight: 700;
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  align-items: center;
`;

const layout = {
  labelCol: {
    span: 8,
  },
  wrapperCol: {
    span: 16,
  },
};

const Items = () => {
  const [visible, updateVisible] = React.useState(false);
  const [api, context] = notification.useNotification();

  const [form] = Form.useForm();

  const { data: allItems = [], reload } = useFetchItems();

  const { user = {} } = React.useContext(ShopContext);

  const sellerItems = allItems.filter(v => v.user_id === user.id);

  const handleOnCancel = () => {
    updateVisible(false);
    form.resetFields();
  };

  const onFinish = async (values) => {
    const authToken = localStorage.getItem('access_token');
    try {
      const response = await post(ADD_ITEM_URL, {
        ...values,
        itemType: 'MAGAZINE'
      }, authToken);
      if (responseOk(response)) {
        await response.json();
        reload();
        updateVisible(false);
        form.resetFields();
        api.success({
          placement: 'topRight',
          message: 'Item added successsfully'
        });
        return;
      }
      api.error({
        placement: 'topRight',
        message: 'Failed to add item'
      })
    } catch (error) {
      console.error(error);
    }

  };

  return (
    <Container>
      {context}
      {sellerItems.map(({ id, title, description, img = '../img/book.jpg', price }) => {
        return (
          <CardWrapper key={id}>
            <Card
              cover={
                <img alt={`${id}-example`} src={img} style={{ height: 150 }} />
              }
            >
              <Meta
                title={title}
                description={
                  <Description>
                    <p>{description}</p>
                    <div className="price">{`Price: ${price} EUR`}</div>
                  </Description>
                }
              />
            </Card>
          </CardWrapper>
        );
      })}
      <ButtonContainer onClick={() => updateVisible(true)}><Button type="dashed" >Add item</Button></ButtonContainer>
      <Modal title="Add item" visible={visible} footer={null} onCancel={handleOnCancel}>
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
            label="Name"
            name="name"
            rules={[
              {
                required: true,
                message: 'Please input item name',
              },
            ]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Description"
            name="description"
            rules={[
              {
                required: true,
                message: 'Please input item description!',
              },
            ]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Price"
            name="price"
            rules={[
              {
                required: true,
                message: 'Please input item price!',
              },
            ]}
          >
            <Input type="number" />
          </Form.Item>

          <Button type="primary" block htmlType="submit">
            Add item
        </Button>
        </Form>
      </Modal>
    </Container>
  );

};

export default Items;