import React, { PureComponent, Fragment } from 'react';
import { connect } from 'dva';
import moment from 'moment';
import { Row, Col, Card, Form, Input, Select, Icon, Button, Radio, Dropdown, Menu, InputNumber, DatePicker, Modal, message, Badge, Divider, Table } from 'antd';
import StandardTable from 'components/StandardTable';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';

import styles from './ClientList.less';

const FormItem = Form.Item;
const { Option } = Select;
const getValue = obj => Object.keys(obj).map(key => obj[key]).join(',');
const columns = [
  {
    title: '序号',
    dataIndex: 'no',
    render: val => {
      return val;
    }
  },
  {
    title: '车牌号码',
    dataIndex: 'carNumber',
  },
  {
    title: '车主姓名',
    dataIndex: 'name',
  },
  {
    title: '手机号码',
    dataIndex: 'phoneNumber',
  },
  {
    title: '保险到期',
    dataIndex: 'limitDate',
  },
  {
    title: '报价时间',
    dataIndex: 'time',
  },
  {
    title: '报价状态',
    dataIndex: 'status',
  },
  {
    title: '来源渠道',
    dataIndex: 'from',
  }
];
const alreadyColumns = [
  {
    title: '序号',
    dataIndex: 'no',
    render: val => {
      return val;
    }
  },
  {
    title: '车牌号码',
    dataIndex: 'carNumber',
  },
  {
    title: '车主姓名',
    dataIndex: 'name',
  },
  {
    title: '手机号码',
    dataIndex: 'phoneNumber',
  },
  {
    title: '订单编号',
    dataIndex: 'limitDate',
  },
  {
    title: '订单时间',
    dataIndex: 'time',
  },
  {
    title: '订单状态',
    dataIndex: 'status',
  },
  {
    title: '是否返现',
    dataIndex: 'from',
  },
  {
    title: '返现金额',
    dataIndex: 'money',
  },
  {
    title: '操作',
    // dataIndex: 'from', 
    render: val => {
      return <div>
        <a>配送</a>
        <a>返现</a>
      </div>
    }
  }
];

const CreateForm = Form.create()((props) => {
  const { modalVisible, form, handleAdd, handleModalVisible } = props;
  const okHandle = () => {
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      form.resetFields();
      handleAdd(fieldsValue);
    });
  };
  return (
    <Modal
      title="新建规则"
      visible={modalVisible}
      onOk={okHandle}
      onCancel={() => handleModalVisible()}
    >
      <FormItem
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 15 }}
        label="描述"
      >
        {form.getFieldDecorator('desc', {
          rules: [{ required: true, message: 'Please input some description...' }],
        })(
          <Input placeholder="请输入" />
        )}
      </FormItem>
    </Modal>
  );
});

@connect(({ rule, loading }) => ({
  rule,
  loading: loading.models.rule,
}))
@Form.create()
export default class ClientList extends PureComponent {
  state = {
    modalVisible: false,
    expandForm: false,
    formValues: {},
    type: '0' // 0 未投保 1 已投保
  };

  componentDidMount() {
    // const { dispatch } = this.props;
    // dispatch({
    //   type: 'rule/fetch',
    // });
  }

  handleFormReset = () => {
    const { form } = this.props;
    form.resetFields();
    this.setState({
      formValues: {},
    });
  }

  toggleForm = () => {
    this.setState({
      expandForm: !this.state.expandForm,
    });
  }

  handleSearch = (e) => {
    e.preventDefault();

    const { form } = this.props;

    form.validateFields((err, fieldsValue) => {
      if (err) return;
      console.log(fieldsValue);
      const values = {
        ...fieldsValue,
        updatedAt: fieldsValue.updatedAt && fieldsValue.updatedAt.valueOf(),
      };

      this.setState({
        formValues: values,
      });
    });
  }

  handleModalVisible = (flag) => {
    this.setState({
      modalVisible: !!flag,
    });
  }

  handleAdd = (fields) => {

    message.success('添加成功');
    this.setState({
      modalVisible: false,
    });
  }

  renderSimpleForm() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={6} sm={24}>
            <FormItem label="车牌号码">
              {getFieldDecorator('carNumber')(
                <Input placeholder="请输入" />
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="订单状态">
              {getFieldDecorator('orderStatus')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="0">待签收</Option>
                  <Option value="1">待配送</Option>
                  <Option value="2">已签收</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="是否返现">
              {getFieldDecorator('isReturn')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="0">否</Option>
                  <Option value="1">是</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">查询</Button>
              <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>重置</Button>
              {/* <a style={{ marginLeft: 8 }} onClick={this.toggleForm}>
                展开 <Icon type="down" />
              </a> */}
            </span>
          </Col>
        </Row>
      </Form>
    );
  }

  renderAdvancedForm() {
    const { getFieldDecorator } = this.props.form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={6} sm={24}>
            <FormItem label="车牌号码">
              {getFieldDecorator('carNumber')(
                <Input placeholder="请输入" />
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="手机号码">
              {getFieldDecorator('phoneNumber')(
                <Input placeholder="请输入" />
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="来源渠道">
              {getFieldDecorator('from')(
                <Input placeholder="请输入" />
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="报价状态">
              {getFieldDecorator('check’')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="0">待报价</Option>
                  <Option value="1">已报价</Option>
                  <Option value="3">核保成功</Option>
                  <Option value="4">核保失败</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        {/* <Row gutter={{ md: 8, lg: 24, xl: 48 }}>
          <Col md={8} sm={24}>
            <FormItem label="更新日期">
              {getFieldDecorator('date')(
                <DatePicker style={{ width: '100%' }} placeholder="请输入更新日期" />
              )}
            </FormItem>
          </Col> 
          <Col md={12} sm={24}>
            <FormItem label="来源渠道">
            {getFieldDecorator('from')(
                <Input placeholder="请输入" />
              )}
            </FormItem>
          </Col>
          <Col md={12} sm={24}>
            <FormItem label="报价状态">
              {getFieldDecorator('check’')(
                <Select placeholder="请选择" style={{ width: '100%' }}>
                  <Option value="0">待报价</Option>
                  <Option value="1">已报价</Option>
                  <Option value="3">核保成功</Option>
                  <Option value="4">核保失败</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          
        </Row> */}
        <div style={{ overflow: 'hidden' }}>
          <span style={{ float: 'right', marginBottom: 24 }}>
            <Button type="primary" htmlType="submit">查询</Button>
            <Button style={{ marginLeft: 8 }} onClick={this.handleFormReset}>重置</Button>
            {/* <a style={{ marginLeft: 8 }} onClick={this.toggleForm}>
              收起 <Icon type="up" />
            </a> */}
          </span>
        </div>
      </Form>
    );
  }

  renderForm() {
    return this.state.type === '1' ? this.renderAdvancedForm() : this.renderSimpleForm();
  }

  handleTypeChange = (e) => {
    this.setState({ type: e.target.value });
  }
  render() {
    const { rule: { data }, loading } = this.props;
    const { modalVisible, type } = this.state;

    const parentMethods = {
      handleAdd: this.handleAdd,
      handleModalVisible: this.handleModalVisible,
    };
    let tableColumns;
    if (type === '0') {
      tableColumns = columns;
    } else {
      tableColumns = alreadyColumns;
    }

    return (
      <PageHeaderLayout title="车险客户">
        <Card bordered={false}>
          <div className={styles.tableList}>
            <div className={styles.tableListOperator}>
              <Radio.Group value={this.state.type} onChange={this.handleTypeChange} size='large' type="primary">
                <Radio.Button value='0' >未投保</Radio.Button>
                <Radio.Button value='1'>已投保</Radio.Button>
              </Radio.Group>
            </div>
            <div className={styles.tableListForm}>
              {this.renderForm()}
            </div>
            <div className={styles.tableListOperator}>
              <Button type="primary" icon="download" size='large'>导出</Button>
            </div>
            <Table
              loading={loading}
              // data={data}
              columns={tableColumns}
            />
          </div>
        </Card>
        <CreateForm
          {...parentMethods}
          modalVisible={modalVisible}
        />
      </PageHeaderLayout >
    );
  }
}
