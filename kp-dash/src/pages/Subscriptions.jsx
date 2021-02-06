import React from 'react';
import styled from 'styled-components';

import { Card, notification } from 'antd';
import { ShoppingCartOutlined } from '@ant-design/icons';
import useFetchMemberships from '../hooks/useFetchMemberships';
import { responseOk } from '../utils/responseOk';
import { post } from '../services/api';
import { PAYPAL_SUBSCRIPTION } from '../constants/url';


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

const { Meta } = Card;


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

const Subscriptions = () => {
  const [api, context] = notification.useNotification();

  const { data: memberships = [] } = useFetchMemberships();

  const handleClick = async ({ id: itemId, name, description }) => {
    const { error = true } = await subscribeMagazine({
      itemId,
      name,
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
      {memberships.map(({ id, name, description, img = '../img/discount.png', price }) => {
        return (
          <CardWrapper key={id}>
            <Card
              cover={
                <img alt={`${id}-example`} src={img} style={{ height: 150 }} />
              }
              actions={[
                <div key="Shop" onClick={() => handleClick({ id, name, description })}>
                  <ShoppingCartOutlined
                    key="Shop"
                    style={{ marginRight: 10 }}
                  />
                  <span>Buy membership</span>
                </div>
              ]}
            >
              <Meta
                title={name}
                description={
                  <Description>
                    <p><strong>{description}</strong></p>
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

export default Subscriptions;

