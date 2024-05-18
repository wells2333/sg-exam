<template>
  <div class="footer">
    <div class="container">
      <el-row :gutter="20">
        <div class="copyright">
          <h4 class="content-c2" style="text-align: center;">Copyright © 2019-2024 {{ sysConfig.sys_web_name }}</h4>
          <ul class="privacy">
            <li class="content-c1"><a class="content-c0"
                                      @click="openContactUs">{{ $t('common.contactUs') }}</a></li>
            <li class="content-c1"><a class="content-c0"
                                      @click="openPrivacy">{{ $t('common.privacyPolicy') }}</a></li>
          </ul>
        </div>
      </el-row>
      <el-row :gutter="20">
        <div class="cop">
          <a class="content-c3" href="https://beian.miit.gov.cn/" target="_blank">
            <span class="content-c3">{{ sysConfig.sys_web_copyright }}</span>
          </a>
        </div>
      </el-row>
    </div>
  </div>
</template>
<script>
import {mapGetters} from 'vuex'

const mail = '1633736729@qq.com';

const zhPolicy = '欢迎您访问我们的产品。本系统 （包括网站等产品提供的服务，以下简称“产品和服务”）是由  无 （以下简称“我们”）开发并运营的。 确保用户的数据安全和隐私保护是我们的首要任务， 本隐私政策载明了您访问和使用我们的产品和服务时所收集的数据及其处理方式。  <br>' +
  '<p>请您在继续使用我们的产品前务必认真仔细阅读并确认充分理解本隐私政策全部规则和要点， 一旦您选择使用，即视为您同意本隐私政策的全部内容，同意我们按其收集和使用您的相关信息。 如您在在阅读过程中，对本政策有任何疑问，可联系我们的客服咨询， 请通过 ' + mail + ' 或产品中的反馈方式与我们取得联系。 如您不同意相关协议或其中的任何条款的，您应停止使用我们的产品和服务。</p>' +
  '本隐私政策帮助您了解以下内容： <br>' +
  '<p>一、我们如何收集和使用您的个人信息；</p>' +
  '<p>二、我们如何存储和保护您的个人信息；</p>' +
  '<p>三、我们如何共享、转让、公开披露您的个人信息；</p>' +
  '<p>四、我们如何使用 Cookie 和其他追踪技术；</p>' +
  '<h3>一、我们如何收集和使用您的个人信息</h3>' +
  '<p>个人信息是指以电子或者其他方式记录的能够单独或者与其他信息， 结合识别特定自然人身份或者反映特定自然人活动情况的各种信息。 我们根据《中华人民共和国网络安全法》和《信息安全技术个人信息安全规范》（GB/T 35273-2017） 以及其它相关法律法规的要求，并严格遵循正当、合法、必要的原则， 出于您使用我们提供的服务和/或产品等过程中而收集和使用您的个人信息，包括但不限于电话号码等。  为接受我们全面的产品服务，您应首先注册一个用户账号，我们将通过它记录相关的数据。 您所提供的所有信息均来自于您本人在注册时提供的数据。 您准备使用的账户名、密码、您本人的联系方式， 我们可能通过发短信或者邮件的方式来验证您的身份是否有效。</p>' +
  '<h3>二、我们如何存储和保护您的个人信息</h3>' +
  '<p>作为一般规则，我们仅在实现信息收集目的所需的时间内保留您的个人信息。 我们会在对于管理与您之间的关系严格必要的时间内保留您的个人信息 （例如，当您开立帐户，从我们的产品获取服务时）。 出于遵守法律义务或为证明某项权利或合同满足适用的诉讼时效要求的目的， 我们可能需要在上述期限到期后保留您存档的个人信息，并且无法按您的要求删除。  当您的个人信息对于我们的法定义务或法定时效对应的目的或档案不再必要时， 我们确保将其完全删除或匿名化。  我们使用符合业界标准的安全防护措施保护您提供的个人信息，并加密其中的关键数据， 防止其遭到未经授权访问、公开披露、使用、修改、损坏或丢失。我们会采取一切合理可行的措施，保护您的个人信息。 我们会使用加密技术确保数据的保密性；我们会使用受信赖的保护机制防止数据遭到恶意攻击。</p>' +
  '<h3>三、我们如何共享、转让、公开披露您的个人信息</h3>' +
  '<p>在管理我们的日常业务活动所需要时，为追求合法利益以更好地服务客户， 我们将合规且恰当的使用您的个人信息。出于对业务和各个方面的综合考虑， 我们仅自身使用这些数据，不与任何第三方分享。  我们可能会根据法律法规规定，或按政府主管部门的强制性要求，对外共享您的个人信息。 在符合法律法规的前提下，当我们收到上述披露信息的请求时，我们会要求必须出具与之相应的法律文件，如传票或调查函。 我们坚信，对于要求我们提供的信息，应该在法律允许的范围内尽可能保持透明。  在以下情形中，共享、转让、公开披露您的个人信息无需事先征得您的授权同意：</p>' +
  '        1、与国家安全、国防安全直接相关的；<br>' +
  '        2、与犯罪侦查、起诉、审判和判决执行等直接相关的； <br>' +
  '        3、出于维护您或其他个人的生命、财产等重大合法权益但又很难得到本人同意的； <br>' +
  '        4、您自行向社会公众公开的个人信息； <br>' +
  '        5、从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道。 <br>' +
  '        6、根据个人信息主体要求签订和履行合同所必需的； <br>' +
  '        7、用于维护所提供的产品或服务的安全稳定运行所必需的，例如发现、处置产品或服务的故障； <br>' +
  '        8、法律法规规定的其他情形。 <br>' +
  '<h3>四、我们如何使用 Cookie 和其他追踪技术</h3>' +
  '<p>为确保产品正常运转，我们会在您的计算机或移动设备上存储名为 Cookie 的小数据文件。 Cookie 通常包含标识符、产品名称以及一些号码和字符。 借助于 Cookie，我们能够存储您的偏好或商品等数据，并用以判断注册用户是否已经登录， 提升服务和产品质量及优化用户体验。</p>' +
  '<p>我们出于不同的目的使用各种Cookie，包括：严格必要型Cookie、性能Cookie、营销Cookie和功能Cookie。 某些Cookie可能由外部第三方提供，以向我们的产品提供其它功能。 我们不会将 Cookie 用于本政策所述目的之外的任何用途。您可根据自己的偏好管理或删除 Cookie。 您可以清除计算机上或手机中保存的所有 Cookie，大部分网络浏览器都设有阻止或禁用 Cookie 的功能， 您可对浏览器进行配置。阻止或禁用 Cookie 功能后，可能影响您使用或不能充分使用我们的产品和服务。</p>'

const enPolicy = 'Welcome to our product. The system (including the services provided by the website and other products, collectively referred to as "the product and services") is developed and operated by Wu (hereinafter referred to as "we"). Ensuring the safety and privacy protection of users\' data is our top priority, and this privacy policy outlines the data collected and processed when you access and use our product and services.  <br>' +
  '<p>Please read and confirm the rules and points of this privacy policy carefully before continuing to use our products, as it is your responsibility to fully understand all the rules and points of this policy. Once you choose to use it, it is deemed that you agree to the entire content of this privacy policy, agreeing that we will collect and use your relevant information according to it. If you have any questions about this policy during the reading process, please contact our customer service for consultation. You can contact us by email at ' + mail + ' or through the feedback function in the product. If you do not agree with any of the terms or provisions of this agreement, you should stop using our products and services. </p>' +
  'This privacy policy helps you understand the following:<br>' +
  '<p>1. How we collect and use your personal information;</p>' +
  '<p>2. How we store and protect your personal information;</p>' +
  '<p>3. How we share, transfer, and publicly disclose your personal information;</p>' +
  '<p>4. How we use Cookie and other tracking technologies; One, How We Collect and Use Your Personal Information</p>' +
  '<h3>I. How do we collect and use your Personal information</h3>' +
  '<p>Personal information refers to any information, whether in electronic or other form, that can be used alone or in combination with other information to identify a specific natural person or reflect the activities of a specific natural person. We collect and use your personal information in accordance with the "Law of the People\'s Republic of China on Network Security" and the "Technical Specification for Personal Information Security" (GB/T 35273-2017) and other relevant laws and regulations, and strictly adhere to the principle of legality, necessity, and legitimacy, in order to collect and use your personal information in the process of using our services and/or products, including but not limited to phone numbers. In order to fully enjoy our products and services, you should first register an account. We will record relevant data through it. All the information you provide comes from the data you provided when registering. The account name, password, and your contact information may be verified by sending text messages or emails.</p>' +
  '<h3>II. How We Store and Protect Your Personal Information</h3>' +
  '<p>As a general rule, we only retain your personal information for as long as necessary to achieve the purpose of collecting information. We will retain your personal information for as long as necessary to manage our relationship with you (for example, when you open an account or receive services from our products). We may need to retain your archived personal information beyond the above-mentioned period for compliance with legal obligations or to prove that a right or contract satisfies applicable statute of limitations requirements. In such cases, we cannot delete your personal information upon your request. When your personal information is no longer necessary for our legal or statute of limitations purposes or archiving, we ensure that it is completely deleted or anonymized. We use industry-standard security measures to protect the personal information you provide, and encrypt key data to prevent unauthorized access, disclosure, use, modification, damage, or loss. We take all reasonable steps to protect your personal information. We use encryption to ensure data confidentiality; we use trusted protection mechanisms to prevent data from being attacked maliciously.</p>' +
  '<h3>III. How we share, transfer, and disclose your personal information</h3>' +
  '<p>We will use your personal information in a compliant and appropriate manner to better serve our customers in managing our daily business activities, pursuing legitimate interests. For comprehensive consideration of business and various aspects, we only use these data ourselves and do not share them with any third party. We may share your personal information with third parties in accordance with laws and regulations or under the compulsory requirements of government departments. In compliance with laws and regulations, we will require legal documents such as subpoenas or investigation letters when we receive requests for disclosure of information. We firmly believe that personal information that we are required to provide should be kept transparent within the limits of the law. Your personal information will not be shared, transferred, or publicly disclosed without your prior authorization in the following cases:</p>' +
  '    1. Those directly related to national security and defense security;<br>' +
  '    2. Those directly related to criminal investigation, prosecution, trial, and execution of sentences;<br>' +
  '    3. Those necessary to protect your or other individuals\' life, property, etc. when it is difficult to obtain your prior consent;<br>' +
  '    4. Personal information you have disclosed to the public yourself;<br>' +
  '    5. Those collected from legally disclosed information, such as legitimate news reports and government information disclosure channels.<br>' +
  '    6. Those necessary to sign and perform a contract as requested by the personal information subject. 7. Necessary for the maintenance of the products or services provided, such as detecting and handling faults in the products or services;<br>' +
  '    8. Other situations stipulated by laws and regulations.<br>' +
  '<h3>IV. How do we use cookies and other tracking technologies</h3>' +
  '<p>How we use Cookies and other tracking technologies to ensure the normal operation of our products, we will store small data files called Cookies on your computer or mobile device. Cookies usually contain identifiers, product names, and some numbers and characters. With the help of Cookies, we can store your preferences or products, and use them to determine whether registered users have logged in, improve service and product quality, and optimize the user experience.</p>' +
  '<p>We use various Cookies for different purposes, including strictly necessary Cookies, performance Cookies, marketing Cookies, and functional Cookies. Some Cookies may be provided by external third parties to provide other functions to our products. We will not use Cookies for any purpose other than those described in this policy. You can manage or delete Cookies according to your preferences. You can clear all Cookies saved on your computer or mobile device, and most web browsers have a function to block or disable Cookies. You can configure your browser. Disabling or blocking the Cookie function may affect your use or inadequate use of our products and services.</p>'

export default {
  data() {
    return {}
  },
  computed: {
    ...mapGetters([
      'sysConfig'
    ])
  },
  methods: {
    openContactUs() {
      this.$notify.info({
        title: this.$t('common.contactUs'),
        message: this.$t('common.contactUsContent'),
        offset: 70
      })
    },
    openPrivacy() {
      let policy = zhPolicy
      if (this.$i18n.locale === 'en') {
        policy = enPolicy
      }
      this.$alert(policy, this.$t('common.privacyPolicy'), {
        customClass: 'privacy-policy-box',
        dangerouslyUseHTMLString: true
      });
    }
  },
  components: {}
}
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
@import "../../assets/css/_theme_color.scss";

.footer {
  background-position: 50%;
  background-color: $white-color;
  width: 100%;
  box-sizing: border-box;
  padding: 18px 0;
}

.container {
  width: 1150px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
}

.c5 {
  color: #646464;
  right: -70px;
  position: relative;
}

.copyright {
  justify-content: center;
  color: #434d55;
  font-size: 12px;
  display: flex;
}

.privacy {
  float: left;
  margin: 0 0 0 12px;
}

.content-c0 {
  color: $bg-default;
  cursor: pointer;
  text-decoration: none;

  &:hover {
    color: $bg-default-hover;
  }
}

.content-c1 {
  float: left;
  line-height: 12px;
  padding: 1px 10px 0;
  border-left: 1px solid #ccc;
  list-style: none;
}

.content-c2 {
  float: left;
  height: 15px;
  line-height: 15px;
  color: $text-color;
  margin: 0 0 0 12px;
}

.cop {
  display: flex;
  justify-content: center;
  margin-top: 16px;
  margin-bottom: 18px;
  height: 15px;
}

.content-c3 {
  margin-right: 20px;
  color: $text-color;
  font-size: 12px;
  height: 12px;
  line-height: 1;
}

.footer-col {
  width: 200px;
  float: left;

  a {
    display: block;
    margin: 5px 0;
    font-size: 14px;
    color: $text-color;
    font-weight: normal;
  }

  .we-chat {
    position: absolute;
    right: 0;
    width: 216px;
    z-index: 2;
    padding-top: 260px;
    background-image: url("../../../static/img/core-img/qq.png");
    background-repeat: no-repeat;
    background-position: top;
    text-align: center;
    line-height: 22px;
  }

  .we-chat-new {
    position: absolute;
    width: 216px;
    z-index: 2;
    padding-top: 260px;
    color: $text-color;
    background-image: url("../../../static/img/core-img/qq_new.png");
    background-repeat: no-repeat;
    background-position: top;
    text-align: center;
    line-height: 22px;
  }
}
</style>
