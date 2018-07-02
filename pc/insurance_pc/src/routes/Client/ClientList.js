import React, {PureComponent} from 'react';
import {connect} from 'dva';
import moment from 'moment';
import {Button, Card, Col, Form, Input, message, Modal, Radio, Row, Select, Table} from 'antd';
import PageHeaderLayout from '../../layouts/PageHeaderLayout';
import {
  affirmCashBackRecordInfo,
  affirmDistributionInfo,
  affirmSignForInfo,
  getCashBackRecordByOrderId,
  getClientList,
  getInvoiceInfoByOrderId,
  getOrderSignForInfoByOrderId,
  getReciverByOrderId
} from '../../services/client';
import {Link} from 'dva/router';

import styles from './ClientList.less';

const queryBtnStyle = null;
const FormItem = Form.Item;
const {Option} = Select;
const getValue = obj => Object.keys(obj).map(key => obj[key]).join(',');

const DistributionModalForm = Form.create()(
  class extends React.Component {
    render() {
      const {visible, onCancel, onCreate, form, reciverData, invoiceData} = this.props;
      const {getFieldDecorator} = form;
      return (
        <Modal
          visible={visible}
          title="保单发货"
          onOk={onCreate}
          onCancel={onCancel}
          reciverData={reciverData}
          invoiceData={invoiceData}
        >
          <Card title="收件人信息" bordered={false}>
            <p style={{fontWeight: "bold"}}>收件人姓名</p>
            <p>{reciverData.reciver}</p>
            <p style={{fontWeight: "bold"}}>收件人联系方式</p>
            <p>{reciverData.phone}</p>
            <p style={{fontWeight: "bold"}}>收件人地址</p>
            <p>{reciverData.provincesCities} {reciverData.adressDetail}</p>
          </Card>
          <Card title="发票信息" bordered={false}>
            <p style={{fontWeight: "bold"}}>发票性质</p>
            <p>{invoiceData.nature || '个人'}</p>
            <p style={{fontWeight: "bold"}}>发票类型</p>
            <p>{invoiceData.invoiceType}</p>
            <p style={{fontWeight: "bold"}}>发票抬头</p>
            <p>{invoiceData.invoiceTitle}</p>
          </Card>
          <Card title="运单信息" bordered={false}>
            <Form layout="vertical">
              <FormItem label="快递公司">
                {getFieldDecorator('expressCompany', {
                  rules: [{required: true, message: '请选择一个快递公司！'}],
                })(
                  <Select placeholder="请选择" style={{width: '100%'}}>
                    <Option value="顺丰速运">顺丰速运</Option>
                    <Option value="EMS">EMS</Option>
                    <Option value="申通快递">申通快递</Option>
                  </Select>
                )}
              </FormItem>
              <FormItem label="运单编号">
                {getFieldDecorator('expressNumber', {
                  rules: [{required: true, message: '请输入运单编号！'}],
                })(
                  <Input placeholder="请输入运单编号"/>
                )}
              </FormItem>
              <FormItem label="备注">
                {getFieldDecorator('remark')(<Input/>)}
              </FormItem>
              <FormItem>
                {getFieldDecorator('orderId', {initialValue: reciverData.orderId || ''})(<Input type="hidden"/>)}
              </FormItem>
            </Form>
          </Card>

        </Modal>
      );
    }
  }
);

const SignForModalForm = Form.create()(
  class extends React.Component {
    render() {
      const {visible, onCancel, onCreate, signForData} = this.props;
      return (
        <Modal
          visible={visible}
          okText="确定签收"
          title="签收确认"
          onOk={onCreate}
          onCancel={onCancel}
          signForData={signForData}
        >
          <Card title="运单配送信息" bordered={false}>
            <p style={{fontWeight: "bold"}}>快递公司</p>
            <p>{signForData.expressCompany}</p>
            <p style={{fontWeight: "bold"}}>运单编号</p>
            <p>{signForData.expressNumber}</p>
            <p style={{fontWeight: "bold"}}>备注</p>
            <p>{signForData.remark}</p>
            <p style={{fontWeight: "bold"}}>发货时间</p>
            <p>{moment(signForData.deliveredTime).format('YYYY-MM-DD HH:mm')}</p>
          </Card>
        </Modal>
      );
    }
  }
);

const CashBackModal = Form.create()(
  class extends React.Component {
    render() {
      const {visible, onCancel, onCreate, cashBackData} = this.props;
      return (
        <Modal
          visible={visible}
          title="返现信息"
          onOk={onCreate}
          onCancel={onCancel}
          cashBackData={cashBackData}
        >
          <Card title="返现信息" bordered={false}>
            <p style={{fontWeight: "bold"}}>收款人姓名</p>
            <p>{cashBackData.payee}</p>
            <p style={{fontWeight: "bold"}}>收款人卡号</p>
            <p>{cashBackData.account}</p>
            <p style={{fontWeight: "bold"}}>开户银行</p>
            <p>{cashBackData.bankname}</p>
            <p style={{fontWeight: "bold"}}>返现金额</p>
            <p>{cashBackData.backMoney}</p>
          </Card>
        </Modal>
      );
    }
  }
);

@connect(({chart, loading}) => {
  return ({
    chart,
    loading: loading.models.chart,
  })
})
@Form.create()
export default class ClientList extends PureComponent {
  state = {
    page: 1,
    expandForm: false,
    distributionModalVisible: false,
    signForModalVisible: false,
    cashBackModalVisible: false,
    formValues: {},
    type: '0', // 0 未投保 1 已投保,
    quotingData: [],
    quotedData: [],
    pagination: {total: 0},
    fieldsValues: {},
    reciverData: {
      reciver: '',
      phone: '',
      provincesCities: '',
      adressDetail: ''
    },
    invoiceData: {
      nature: '',
      invoiceType: '',
      invoiceTitle: ''
    },
    signForData: {
      expressCompany: '',
      expressNumber: '',
      remark: '',
      deliveredTime: ''
    },
    cashBackData: {
      payee: '',
      account: '',
      bankname: '',
      backMoney: ''
    }
  }
  ;
  //封装请求数据
  getQueryData = (values) => {
    let queryData = {...values};
    this.state.type == '0' ? queryData.toubao = false : queryData.toubao = true;
    if (values.cashback) {
      values.cashback == '0' ? queryData.cashback = false : queryData.cashback = true;
    }
    return queryData;
  }
  getClientList = (values, page) => {
    let queryData = this.getQueryData(values)
    queryData.page = page;
    queryData.number = 10;
    if (page == 1) {
      this.setState({
        pagination: {total: 0, current: 1}
      })
    }
    getClientList({
      ...queryData
    }).then(res => {
      console.log('查询结果：');
      console.log(res);
      if (res && res.code == 0) {
        const resultData = res.data;
        resultData.map((item, index) => {
          resultData[index].key = item.id
        });
        this.setState({
          quotingData: resultData,
          pagination: {total: res.counts}
        })
      }
    }).catch(err => {

    })
  };
  handleFormReset = () => {
    const {form, dispatch} = this.props;
    form.resetFields();
    this.setState({
      formValues: {},
    });
    // dispatch({
    //   type: 'client/fetch',
    //   payload: {}
    // });
  };
  //查询
  handleSearch = (e) => {
    e.preventDefault();

    const {form} = this.props;
    form.validateFields((err, fieldsValues) => {
      if (err) return;
      // console.log(fieldsValues);
      // this.state.quotingData;
      this.setState({
        fieldsValues: e.target.value
      });
      const values = {
        ...fieldsValues,
        updatedAt: fieldsValues.updatedAt && fieldsValues.updatedAt.valueOf(),
      };

      this.getClientList(values, 1);
    });
  };
  handleTypeChange = (e) => {
    this.setState({quotingData: [], quotedData: []});
    this.setState({type: e.target.value}, () => this.getClientList({}, 1));
  }
  handleTableChange = (pagination, data) => {
    const pager = {...this.state.pagination};
    pager.current = pagination.current;
    this.setState({
      pagination: pager,
      page: pager.current
    })
    console.log(pagination)
    this.getClientList(this.state.fieldsValues, pagination.current)
  }


  //显示配送窗口
  showDistributionModal = (item) => {
    if (item.orderId) {
      //发送异步请求，获取收件人信息
      getReciverByOrderId({
        orderId: item.orderId
      }).then(res => {
        if (res) {
          this.setState({
            reciverData: JSON.parse(JSON.stringify(res))
          });
        } else {
          this.state.reciverData = {
            reciver: '',
            phone: '',
            provincesCities: '',
            adressDetail: ''
          }
        }
      });
      //发送异步请求，获取发票信息
      getInvoiceInfoByOrderId({
        orderId: item.orderId
      }).then(res => {
        if (res) {
          console.log(res);
          this.setState({
            invoiceData: JSON.parse(JSON.stringify(res))
          });
        } else {
          this.state.invoiceData = {
            nature: '',
            invoiceType: '',
            invoiceTitle: ''
          }
        }
      });
      this.setState({
        distributionModalVisible: true
      });

    } else {
      console.log("orderId为空，请联系管理员！")
    }
  };

  //配送弹窗：点击确定
  handleDistributionForm = () => {
    const form = this.distributionFormRef.props.form;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }
      affirmDistributionInfo({
        ...values
      }).then(res => {
        if (res && res.code == 0) {
          document.getElementById("searchBtn").click();
          form.resetFields();
          this.setState({distributionModalVisible: false});
          message.success(res.msg);
        } else {
          console.log(res);
          message.error(res.msg);
        }
      });

    });
  };

  //配送窗口的取消事件
  handleCancelDistributionForm = () => {
    const form = this.distributionFormRef.props.form;
    form.resetFields();
    this.setState({distributionModalVisible: false});
  };

  //配送窗口的form表单
  saveDistributionFormRef = (distributionFormRef) => {
    this.distributionFormRef = distributionFormRef;
  };

  //显示签收弹窗
  showSignForInfoModal = (item) => {
    if (item.orderId) {
      //发送异步请求，获取运单信息
      getOrderSignForInfoByOrderId({
        orderId: item.orderId
      }).then(res => {
        if (res) {
          this.setState({
            signForData: JSON.parse(JSON.stringify(res))
          });
        }
      });
      this.setState({
        signForModalVisible: true
      });

    } else {
      console.log("orderId为空，请联系管理员！")
    }
  };

  //签收弹窗：点击确定
  handleSignForForm = () => {
    affirmSignForInfo({
      orderId: this.state.signForData.orderId
    }).then(res => {
      if (res && res.code == 0) {
        document.getElementById("searchBtn").click();
        this.setState({signForModalVisible: false});
        message.success(res.msg);
      } else {
        console.log(res);
        message.error(res.msg);
      }
    });
  };

  //签收弹窗：取消事件
  handleCancelSignForForm = () => {
    this.setState({signForModalVisible: false, signForData: {}});
  };

  //显示返现弹窗
  showCashBackModal = (item) => {
    if (item.orderId) {
      //发送异步请求，获取返现
      getCashBackRecordByOrderId({
        orderId: item.orderId
      }).then(res => {
        if (res) {
          res["backMoney"] = item.backMoney;
          res["orderId"] = item.orderId;
          this.setState({
            cashBackData: JSON.parse(JSON.stringify(res))
          });
        }
      });
      this.setState({
        cashBackModalVisible: true
      });

    } else {
      msessage.error("orderId为空，请联系管理员！");
    }
  };

  //返现弹窗：点击确定
  handleCashBackForm = () => {
    affirmCashBackRecordInfo({
      orderId: this.state.cashBackData.orderId
    }).then(res => {
      if (res && res.code == 0) {
        document.getElementById("searchBtn").click();
        this.setState({cashBackModalVisible: false});
        message.success(res.msg);
      } else {
        console.log(res);
        message.error(res.msg);
      }
    });

  };

  //返现弹窗：取消事件
  handleCancelCashBackForm = () => {
    this.setState({cashBackModalVisible: false});
  };

  componentDidMount() {
    this.getClientList({
      toubao: false
    }, 1);
    // dispatch({
    //   type: 'rule/fetch',
    // });
  }

  // exprotExcel = () => {   //导出excel  TODO

  // }

  //动态获得头部筛选字段
  renderSimpleForm() {
    const {getFieldDecorator} = this.props.form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline" id="searchForm">
        <Row gutter={{md: 8, lg: 24, xl: 48}}>
          <Col md={6} sm={24}>
            <FormItem label="车牌号码">
              {getFieldDecorator('licenseNumber')(
                <Input placeholder="请输入"/>
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="订单状态">
              {getFieldDecorator('quoteStateCode')(
                <Select placeholder="请选择" style={{width: '100%'}}>
                  <Option value="">全部</Option>
                  <Option value="3">承保中</Option>
                  <Option value="4">承保失败</Option>
                  <Option value="5">待配送</Option>
                  <Option value="6">待签收</Option>
                  <Option value="7">已投保</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="是否返现">
              {getFieldDecorator('cashback')(
                <Select placeholder="请选择" style={{width: '100%'}}>
                  <Option value=''>全部</Option>
                  <Option value='0'>否</Option>
                  <Option value='1'>是</Option>
                </Select>
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
                        <span className={styles.submitButtons}>
                            <Button type="primary" htmlType="submit" id="searchBtn">查询</Button>
                            <Button style={{marginLeft: 8}} onClick={this.handleFormReset}>重置</Button>
                        </span>
          </Col>
        </Row>
      </Form>
    );
  }

  renderAdvancedForm() {
    const {getFieldDecorator} = this.props.form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row gutter={{md: 8, lg: 24, xl: 48}}>
          <Col md={6} sm={24}>
            <FormItem label="车牌号码">
              {getFieldDecorator('licenseNumber')(
                <Input placeholder="请输入"/>
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="手机号码">
              {getFieldDecorator('phone')(
                <Input placeholder="请输入"/>
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="来源渠道">
              {getFieldDecorator('source')(
                <Input placeholder="请输入"/>
              )}
            </FormItem>
          </Col>
          <Col md={6} sm={24}>
            <FormItem label="报价状态">
              {getFieldDecorator('quoteStateCode')(
                <Select placeholder="请选择" style={{width: '100%'}}>
                  <Option value="">全部</Option>
                  <Option value="8">待报价</Option>
                  <Option value="0">核保中</Option>
                  <Option value="1">核保失败</Option>
                  <Option value="2">未支付</Option>
                </Select>
              )}
            </FormItem>
          </Col>
        </Row>
        <div style={{overflow: 'hidden'}}>
                    <span style={{float: 'right', marginBottom: 24}}>
                        <Button type="primary" htmlType="submit" onClick={this.handleSearch}>查询</Button>
                        <Button style={{marginLeft: 8}} onClick={this.handleFormReset}>重置</Button>
                    </span>
        </div>
      </Form>
    );
  }

  renderForm() {
    return this.state.type === '0' ? this.renderAdvancedForm() : this.renderSimpleForm();
  }

  render() {
    const {type} = this.state;
    const columns = [
      {
        title: '序号',
        // dataIndex: 'key',
        render: (text, record, index) => {
          return index + 1 + (this.state.page - 1) * 10;
        }
      },
      {
        title: '车牌号码',
        dataIndex: 'licenseNumber',
        render: (text, record) => {
          return <Link to={`/client/detail/${record.id}`}>
            {text}
          </Link>
        }
      },
      {
        title: '车主姓名',
        dataIndex: 'ownerName',
      },
      {
        title: '手机号码',
        dataIndex: 'phone',
      },
      {
        title: '保险到期',
        dataIndex: 'insuranceDeadline',
        render: val => val ? <span>{moment(val).format('YYYY-MM-DD HH:mm')}</span> : '',
      },
      {
        title: '报价时间',
        dataIndex: 'leastQuoteTime',
        render: val => val ? <span>{moment(val).format('YYYY-MM-DD HH:mm')}</span> : '',
      },
      {
        title: '报价状态',
        dataIndex: 'quoteStateCode',
        render: val => {
          switch (val) {
            case 0:
              return '核保中';
            case 1:
              return '核保失败';
            case 2:
              return '未支付';
            case 3:
              return '承保中';
            case 4:
              return '承保失败';
            case 5:
              return '待配送';
            case 6:
              return '待签收';
            case 7:
              return '已投保';
            case 8:
              return '待报价';

          }
        }
      },
      {
        title: '来源渠道',
        dataIndex: 'source',
      }
    ];
    const alreadyColumns = [
      {
        title: '序号',
        render: (text, record, index) => {
          return index + 1 + (this.state.page - 1) * 10;
        }
      },
      {
        title: '车牌号码',
        dataIndex: 'licenseNumber',
        render: (text, record) => {
          return <Link to={`/client/detail/${record.id}`}>
            {text}
          </Link>
        }
      },
      {
        title: '车主姓名',
        dataIndex: 'ownerName',
      },
      {
        title: '手机号码',
        dataIndex: 'phone',
      },
      {
        title: '订单编号',
        dataIndex: 'orderId',
      },
      {
        title: '订单时间',
        dataIndex: 'leastOrderTime',
        render: val => val ? <span>{moment(val).format('YYYY-MM-DD HH:mm')}</span> : '',
      },
      {
        title: '订单状态',
        dataIndex: 'quoteStateCode',
        render: val => {
          switch (val) {
            case 0:
              return '核保中';
            case 1:
              return '核保失败';
            case 2:
              return '未支付';
            case 3:
              return '承保中';
            case 4:
              return '承保失败';
            case 5:
              return '待配送';
            case 6:
              return '待签收';
            case 7:
              return '已投保';
            case 8:
              return '待报价';
          }
        }
      },
      {
        title: '是否返现',
        dataIndex: 'cashback',
        render: val => {
          if (val) {
            return '已返现';
          } else {
            return '未返现';
          }
        }
      },
      {
        title: '返现金额',
        dataIndex: 'backMoney',
      },
      {
        title: '操作',
        // dataIndex: 'from',
        render: val => {
          let stateCode = val.quoteStateCode;
          let ifBack = val.cashback == true ? '' : '返现';
          switch (stateCode) {
            case 5:
              return <div>
                <a onClick={() => this.showDistributionModal(val)}>配送</a>
                <a style={{marginLeft: 10}}>{ifBack}</a>
              </div>;
            case 6:
              return <div>
                <a onClick={() => this.showSignForInfoModal(val)}>签收</a>
                <a style={{marginLeft: 10}}>{ifBack}</a>
              </div>;
            default:
              return <div>
                <Link to={`/client/detail/${val.id}`}>查看</Link>
                <a style={{marginLeft: 10}} onClick={() => this.showCashBackModal(val)}>{ifBack}</a>
              </div>;
          }
        }
      }
    ];
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
                <Radio.Button value='0'>未投保</Radio.Button>
                <Radio.Button value='1'>已投保</Radio.Button>
              </Radio.Group>
            </div>
            <div className={styles.tableListForm}>
              {this.renderForm()}
            </div>
            <div className={styles.tableListOperator}>
              <a href="/carInsurance/api/excel/exportClient">
                <Button type="primary" icon="download" size='large'>导出</Button>
              </a>
            </div>

            <Table
              // loading={loading}
              // dataSource={this.state.type == '0' ? this.state.quotingData : this.state.quotedData}
              dataSource={this.state.quotingData}
              columns={tableColumns}
              onChange={(pagination) => this.handleTableChange(pagination)}
              pagination={this.state.pagination}
            />
          </div>
        </Card>
        <DistributionModalForm
          wrappedComponentRef={this.saveDistributionFormRef}
          visible={this.state.distributionModalVisible}
          reciverData={this.state.reciverData}
          invoiceData={this.state.invoiceData}
          onCreate={this.handleDistributionForm}
          onCancel={this.handleCancelDistributionForm}
        />
        <SignForModalForm
          visible={this.state.signForModalVisible}
          signForData={this.state.signForData}
          onCreate={this.handleSignForForm}
          onCancel={this.handleCancelSignForForm}
        />
        <CashBackModal
          visible={this.state.cashBackModalVisible}
          cashBackData={this.state.cashBackData}
          onCreate={this.handleCashBackForm}
          onCancel={this.handleCancelCashBackForm}
        />
      </PageHeaderLayout>
    );
  }
}
