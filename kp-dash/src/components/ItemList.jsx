import React from 'react';
import styled from 'styled-components';

const ItemTable = styled.table`
  width: 100%;

  td,
  th {
    padding: 10px 0px;
    width: 20%;
  }

  tbody {
    tr.line-item-row:hover {
      cursor: pointer;
    }
  }

  tfoot > tr > td {
    border-top: 1px solid #F1F3F5;
    font-size: 13px;
    line-height: 14px;
    color: #303942;
    &:last-child {
      font-weight: bold;
      font-size: 14px;
    }
    width: 50%;
  }

  td {
    text-align: right;
    font-size: 13px;
    line-height: 14px;
    color: #303942;
  }

  th {
    border-bottom: 1px solid #F1F3F5;
    text-align: right;
    color: #888888;
    font-size: 10px;
    line-height: 11px;
    font-weight: bold;
  }
  tr > th:first-child,
  tr > td:first-child {
    text-align: left;
    width: 40%;
  }
`;

const ItemList = ({ items = [], user = {} }) => {

  /** `calc` total price of the items */
  const totalPrice = items.reduce((acc, curr) => acc + curr.price * curr.count, 0);

  /** `discount` for the memberships */
  const maxDiscount = Math.max(...(user.subscriptionList || []).map(v => v.discount));

  return (
    <ItemTable>
      <thead>
        <tr>
          {['Item', 'Description', 'Price'].map(header => (
            <th key={header}>{header}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {items.map(item => (
          <tr className="line-item-row" key={item.id}>
            <td>{item.title}</td>
            <td>{item.description}</td>
            <td>{`${item.price} x ${item.count}`}€</td>
          </tr>
        ))}
      </tbody>
      <tfoot>
        <tr>
          <td colSpan={2}>
            Total
          </td>
          <td>{totalPrice}€{maxDiscount && items.length ? ` - ${maxDiscount}% = ${(totalPrice * (1 - maxDiscount / 100)).toFixed(2)}€` : ''}</td>
        </tr>
      </tfoot>
    </ItemTable>
  );
};

export default ItemList;