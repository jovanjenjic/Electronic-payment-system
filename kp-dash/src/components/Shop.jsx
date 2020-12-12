import React from 'react';
import styled from 'styled-components';

import { Card, notification } from 'antd';
import { ShoppingCartOutlined, SendOutlined } from '@ant-design/icons';

import { ShopContext } from '../context/shop';

import { PAYPAL_SUBSCRIPTION } from '../constants/url';
import { post } from '../services/api';
import { responseOk } from '../utils/responseOk';

const { Meta } = Card;

const Container = styled.div`
  display: grid;
  grid-template-columns: 25% 25% 25% 25% 25%;
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

const dummyData = [
  {
    id: 1,
    price: 3,
    description: 'Nice boook',
    title: 'Konan',
    img: '../img/book.jpg',
  },
  {
    id: 2,
    price: 5,
    description: 'Boom',
    title: 'Varvarin',
    img: '../img/book.jpg',
  },
];

const subscribeMagazine = async (data) => {
  try {
    const authToken = localStorage.getItem('access_token');
    const response = await post(PAYPAL_SUBSCRIPTION, data, authToken);

    if (responseOk(response)) {
      const { paymentUrl = '' } = await response.json();
      window.open(paymentUrl);
      return { error: false };
    }
  } catch (error) {
    console.error(error);
  }
  return { error: true };
};

const Shop = () => {
  const [api, context] = notification.useNotification();

  const { updateItems } = React.useContext(ShopContext);

  const handleClick = (id) => {
    updateItems((prevItems) => {
      const foundItem = prevItems.find((v) => v.id === id);
      return foundItem
        ? [{ ...foundItem, count: (foundItem.count || 0) + 1 }]
        : [{ ...dummyData.find((v) => v.id === id), count: 1 }];
    });
  };

  const onSubscribe = async ({ id: itemId, title: name, description }) => {
    const { error = true } = await subscribeMagazine({
      itemId,
      name: `Subscription ${name}`,
      description,
    });
    if (error) {
      api.error({
        placement: 'topRight',
        message: 'Failed to subscribe to the magazine',
      });
    }
  };

  return (
    <Container>
      {context}
      {dummyData.map(({ id, title, description, img, price }) => {
        return (
          <CardWrapper key={id}>
            <Card
              cover={
                <img alt={`${id}-example`} src={img} style={{ height: 150 }} />
              }
              actions={[
                <div key="Shop" onClick={() => handleClick(id)}>
                  <ShoppingCartOutlined
                    key="Shop"
                    style={{ marginRight: 10 }}
                  />
                  <span>Buy</span>
                </div>,
                <div
                  key="Subscribe"
                  onClick={() => onSubscribe({ id, title, description })}
                >
                  {' '}
                  <SendOutlined key="Subscribe" style={{ marginRight: 10 }} />
                  Subscribe
                </div>,
              ]}
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
    </Container>
  );
};

export default Shop;
