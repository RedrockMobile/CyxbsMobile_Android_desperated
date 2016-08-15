package com.excitingboat.freshmanspecial.view.fragment.FreshmanGuide;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.excitingboat.freshmanspecial.R;

/**
 * Created by xushuzhan on 2016/8/4.
 */
public class EnrolInformationFragment extends Fragment implements View.OnClickListener{
    public boolean isShow = false;
    View view;
    TextView  ReportInformation;
    TextView SafetyInformation;
    TextView ScholarshipInformation;
    TextView HandbookInformation;

    ImageButton IV1;
    ImageButton IV2;
    ImageButton IV3;
    ImageButton IV4;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=LayoutInflater.from(getContext()).inflate(R.layout.project_freshman_special__fragment_fg_enrol_information,container,false);
        initview();
        initData();
        return view;
    }


    private void initview() {
        ReportInformation = (TextView) view.findViewById(R.id.report_information_content);
        ReportInformation.setOnClickListener(this);
        SafetyInformation = (TextView) view .findViewById(R.id.safety_information_content);
        SafetyInformation.setOnClickListener(this);
        ScholarshipInformation = (TextView) view .findViewById(R.id.scholarship_information_content);
        ScholarshipInformation.setOnClickListener(this);
        HandbookInformation = (TextView) view .findViewById(R.id.handbook_information_content);
        HandbookInformation.setOnClickListener(this);

        IV1 = (ImageButton) view.findViewById(R.id.ib_report_information);
        IV1.setOnClickListener(this);
        IV2 = (ImageButton) view.findViewById(R.id.ib_safety_information);
        IV2.setOnClickListener(this);
        IV3 = (ImageButton) view.findViewById(R.id.ib_scholarship_information);
        IV3.setOnClickListener(this);
        IV4 = (ImageButton) view.findViewById(R.id.ib_handbook_information);
        IV4.setOnClickListener(this);



    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ib_report_information || i == R.id.report_information_content) {
            if (isShow) {
                ReportInformation.setMaxLines(3);
                IV1.setBackground(getContext().getResources().getDrawable(R.drawable.more_content));
                isShow = false;
            } else {
                ReportInformation.setMaxLines(10000);
                IV1.setBackground(getContext().getResources().getDrawable(R.drawable.hint_content));
                isShow = true;
            }

        } else if (i == R.id.ib_safety_information || i == R.id.safety_information_content) {
            if (isShow) {
                SafetyInformation.setMaxLines(3);
                IV2.setBackground(getContext().getResources().getDrawable(R.drawable.more_content));
                isShow = false;
            } else {
                SafetyInformation.setMaxLines(10000);
                IV2.setBackground(getContext().getResources().getDrawable(R.drawable.hint_content));
                isShow = true;
            }

        } else if (i == R.id.ib_scholarship_information || i == R.id.scholarship_information_content) {
            if (isShow) {
                ScholarshipInformation.setMaxLines(3);
                IV3.setBackground(getContext().getResources().getDrawable(R.drawable.more_content));
                isShow = false;
            } else {
                ScholarshipInformation.setMaxLines(10000);
                IV3.setBackground(getContext().getResources().getDrawable(R.drawable.hint_content));
                isShow = true;
            }

        } else if (i == R.id.ib_handbook_information || i == R.id.handbook_information_content) {
            if (isShow) {
                HandbookInformation.setMaxLines(3);
                IV4.setBackground(getContext().getResources().getDrawable(R.drawable.more_content));
                isShow = false;
            } else {
                HandbookInformation.setMaxLines(10000);
                IV4.setBackground(getContext().getResources().getDrawable(R.drawable.hint_content));
                isShow = true;
            }

        } else {
        }
    }




    private void initData() {
        ReportInformation.setText("一、新生入学时间：2016年9月7日、8日。\n" +
                "新生报到需要物品：报到时需持我校新生录取通知书、高考准考证及身份证三证入学报到，因故不能及时报道者，需要及时向学校请假。未请假或逾期未报道者视为放弃入学资格。\n" +
                "二、报道地点为：重庆邮电大学风雨操场\n" +
                "三、新生档案、户口、党团关系及其他事项办理须知 ：\n" +
                "a、新生档案：\n" +
                "凡明确了档案由学生自带的省市，该省新生必须自带档案入学报到（学生不得私自拆封档案），报到时交至学生所在学院。（需邮寄档案的请寄至所在学院学生办公室，具体如下： “重庆市南岸区南山街道崇文路2号重庆邮电大学×××学院学生办公室”） \n" +
                "b、党团关系 ：\n" +
                "党组织关系办理：\n" +
                "入学新生凡是中共党员（含预备党员）者，其党员组织关系须及时转入我校。党员组织关系介绍信须由县级及县级以上的党委组织部门开具。请使用新版的三联单介绍信，有效期开至9月开学时间。\n" +
                "    重庆市外转入的，介绍信抬头写中共重庆市委教育工委组织干部处，去的单位写重庆邮电大学××学院。入学报到时连同党员材料一起交到所录取学院新生辅导员处，学院收齐后统一送组织部审查。 \n" +
                "     根据重庆市委组织部要求，重庆市内转接组织关系须通过“12371”党建信息平台转接，不需再开纸质介绍信。请新生党员入学前到原单位党组织办理，转往支部请选择“中共重庆市委教育工委---中共重庆邮电大学委员会---录取学院党总支或党支部”。在转接原因处请填写新生入学，并注明录取学院、专业。入学报到时需到所录取学院新生辅导员登记并提交党员材料。【校组织部电话：023-62461130】\n" +
                "团组织关系办理：\n" +
                "入学新生是团员者须带团员证，凭团员证接转团组织关系。如果团员证丢失，必须带上由所在中学出具的团组织关系证明材料，方可报到后补办团员证。且党、团员档案中必备入党、入团的有关材料。【校团委电话：023-62460140】\n" +
                "C、户口关系 \n" +
                "  根据公安机关户籍管理规定，新生入学时是否办理户口迁移凭学生本人自愿，即新生入学时户口可迁可不迁。受南山派出所（我校师生户籍所在地派出所）委托，我校保卫处在新生报到至2016年9月30日前集中代为办理新生户口迁移手续，此后至学生在校读书期间南山派出所均不再受理户口迁移手续。\n" +
                "需办理户口迁移的学生，凭身份证和户口迁移证入户，户口迁入地址：重庆市南岸区南山街道崇文路2号附2号。同时，将本人一寸近期免冠照片贴于迁移证左上角，报到时交给学校保卫处（报到现场设办理点）。\n" +
                "不迁户口的学生，入学报到时填写《未迁户口登记表》（报到现场领取），并贴上一寸近期免冠照片后交给所在学院，由学院汇总后交保卫处管理。户籍咨询电话： \n" +
                "【南山派出所：023-62463666（上班期间）】\n" +
                "【学校保卫处：023-62461150（上班期间）】 \n" +
                "四、\t新生入学缴费须知：\n" +
                "为了方便学生安全、及时缴纳学费、住宿费（以下简称学杂费）及办理入学报到相关手续，现就2015级新生收费安排通知如下： \n" +
                "a.\t具体要求 \n" +
                "1.2014级新生学杂费全部使用学校统一办理的重庆农业银行卡（随录取通知书发放的银行卡）缴纳学费，以后每年学杂费的缴纳都将按此方式进行，请新同学们注意将学校办理的银行卡及本“须知”妥善保存，不要遗失，以免耽误缴费和正常报到。 \n" +
                "2.请登录重庆邮电大学集中收费平台终端查询系统（简称收费平台，网址：http://jzsf.cqupt.edu.cn）查询学杂费缴费情况，并在“基本信息修改”中完善个人联系电话和核实缴费银行卡卡号，点击“同意扣款协议”后进行数据绑定。在收费平台中，可通过农业银行网银进行自助缴费。 \n" +
                "3.根据学校与重庆市农业银行签订的《委托代扣协议书》，我校已委托中国农业银行南山支行代扣学杂费。为了保证学生顺利完成报到注册的各种手续，根据银行相关规定，请及时亲自签写《委托扣款、划款授权书》后将身份证正反面复印件粘贴在其背面，并到就近农业银行网点将相关款项（包含：学费、住宿费、代收费及银行卡年费）存入银行卡。学生入学报到时，将亲自填写并粘贴有身份证复印件的《委托扣款、划款授权书》交给辅导员，本人不需要到财务处办理任何手续，缴费收据将于9月30日由各学院统一发放。 \n" +
                "4.若不能足额存入学费、住宿费、代收费及银行卡年费，请不要将生活费存到账户中，避免被误扣，影响学生日常生活费用开支。 \n" +
                "b.\t用卡须知 \n" +
                "1. 在首次使用银行卡前，应仔细核对姓名、卡号是否相符，如果不相符，请不要使用，并致电023-62463094咨询。 \n" +
                "2．学生须注意用卡安全：银行卡必须由本人保管，在任何情况下学生都不要将银行卡及密码交给或告诉任何人，银行代学校扣缴学费时并不需要银行卡及密码。查询存款余额、交易明细或用卡过程有问题时，请立即致电农行服务热线95599。农行办理的银行卡，学生或学生家长可到任何一个农行网点或自助存款机存入学费、住宿费、代收费和银行卡年费。缴费后剩余存款可在全国任一农行网点或自助提款机上自行取款（注意：该卡在取款前需持本人身份证及银行卡到全国任一农行网点办理激活，激活后方可办理取款业务，仅办理存款不需激活就可办理，激活时输入初始密码，重新设置新密码，初始密码为111111）。为保证资金安全，激活后重新设置的新密码必须保密，只能自已使用，学生发现密码有泄密的可能后，可以在银行自助服务设备上自已修改密码，否则后果自负。到校报到期间，请同学们注意保管好银行卡，最好不要将卡和身份证放在一起，防止丢失时挂失不便。为方便使用ATM，请各位同学在收到银行卡并核对无误后，将银行卡上的纸质标签清除干净。 \n" +
                "3.银行卡服务费：在重庆市任何农业银行网点或自助存取款机内存取款不收取任何费用；在重庆市非农行自助取款机取款，无论金额大小，每笔收取手续费2元。在重庆市以外农行存款按金额的0.5%收取手续费，最低1元，最高50元；取款按金额的1%收取手续费，最低1元，最高100元。实际标准可咨询当地农行网点，银行代扣代缴学杂费不收取手续费。 \n" +
                "4. 如果银行卡被盗或丢失，请立即打电话95599按语音提示进行口头挂失，并于五日内持身份证到重庆市任一农业银行网点办理书面挂失手续（如果在规定期限内不能按要求办理的，可以拨打该电话继续挂失）。开户行:中国农业银行重庆南山支行，地址：重庆邮电大学新校门口右侧。挂失补发新的银行卡后，请及时登录收费平台更新缴费银行卡卡号,以免影响缴费。 \n" +
                "5.办理存款后，学生务必持卡入校，以便查询和个人平时使用。我校附近设有农业银行网点（新校门口右侧）、校内设有农行ATM取款机（1栋宿舍旁），可为学生提供服务。 \n" +
                "c.\t联系方式 \n" +
                "农行业务咨询电话： 95599、023-62463094 \n" +
                "重庆邮电大学财务处咨询电话：023-62461257 \n" +
                "集中收费平台网址：http://jzsf.cqupt.edu.cn\n" +
                "五、助学贷款须知\n" +
                "我校鼓励家庭经济困难学生积极申请办理助学贷款，解决学费和住宿费，顺利完成学业。助学贷款形式及要求如下：\n" +
                "1、\t生源地信用助学贷款\n" +
                "生源地信用助学贷款（简称“生源地贷款”）是指家庭经济困难学生及学生家长通过向生源地相关机构申请办理的助学贷款（约占总人数的20%），由国家开发银行向符合条件的家庭经济困难的普通高校新生和在校生发放的、在学生入学前户籍所在区县（自治县）办理的助学贷款。\n" +
                "自2007年国家在江苏、湖北、重庆、陕西、甘肃5省市试点开办生源地信用助学贷款业务后，现已覆盖全国各省市区。生源地贷款政策性强、办理方便、发放贷款及时、贷款期限长（最长14年），学生在校期间产生的贷款利息由财政全额贴息。每人每年最高可获得6000元。\n" +
                "我校鼓励家庭经济困难学生积极向生源所在地县级教育行政部门（学生资助管理中心）申请办理生源地贷款，同时积极配合各地方政府做好我校学生生源地贷款工作。\n" +
                "2、\t高校国家助学贷款\n" +
                "高校国家助学贷款，是指由学校组织在校家庭经济困难学生向银行申请办理的助学贷款（约占总人数的1%）。国家助学贷款期限最长10年，每人每年可获得与所读专业学费相当的贷款，但最高不超过6000元。\n" +
                "需要在学校向银行申请办理国家助学贷款的学生，请按以下流程提交信息：\n" +
                "（1）登录www.gjzxdk.com，进行注册；\n" +
                "（2）注册成功后登录，进入贷款申请页面，根据实际情况进行选择；\n" +
                "（3）填写申请人相关信息后提交申请。\n" +
                "具体准备材料如下：\n" +
                "(1)《就学地国家助学保证保险贷款申请表》原件1份，登陆www.gjzxdk.com注册完善信息提交申请后在线打印；\n" +
                "(2)家庭经济困难证明原件1份（当地乡、镇或街道民政部门核实并加盖鲜章的《高等学校学生及家庭情况调查表》，①印章清晰；②表中涉及家庭收入栏金额不能涂改，涂改无效；③家庭人均月收入低于350元，人均年收入低于4200元的家庭经济困难学生方可申请；\n" +
                "(3)学生录取通知书（或学生证）复印件1份；\n" +
                "(4)学生身份证复印件2份。\n" +
                "以上所有材料必须使用黑色钢笔或者黑色签字笔填写，不得使用圆珠笔或者铅笔填写。\n" +
                "3、资助工作\n" +
                "重庆邮电大学严格按照国家及重庆市相关学生资助政策，采取多种措施，建立起以奖学金、助学贷款、勤工助学为主，助学金及绿色通道等相配合的完善有效的资助体系。\n" +
                "家庭经济困难学生一方面可以通过勤奋学习，争取获得奖学金，缓解经济压力；另一方面也可以通过申请助学贷款、勤工助学金、国家助学金、社会资助金、“绿色通道”入学、学费减免、临时困难补助等方式顺利完成学业。\n" +
                "我校坚持将经济资助与成长教育相结合，将物质帮困与励志强能相统一，以经济资助为基础，以精神激励为支撑，以能力提升为核心，以成才就业为目标，形成了“帮困励志·助学强能”的教育引导机制。\n" +
                "学生资助管理中心：023-62461577 \n" +
                "六、勤工助学\n" +
                "勤工助学活动是指学生在学校的组织下利用课余时间，通过劳动取得合法报酬，用于改善学习和生活条件的社会实践活动。我校长期向家庭经济困难学生提供校内后勤服务、校园管理、工作助理等近1200个勤工助学岗位，学生在学有余力的情况下可通过勤工助学活动缓解经济压力，并得到实践锻炼。参加学生人均每年可获得2000元的勤工助学酬劳。校勤工助学中心和各学院将于9月份招聘2015级新生从事勤工助学活动。\n" +
                "七、\t其他事项：\n" +
                "1.\t自带同版近期照片共15张（要求光面相纸洗印，白底一寸，半身、正面、免冠大头照片）。 \n" +
                "2.\t学生公寓备有床上生活用品等设施供同学们选购使用，可满足日常生活需要。 \n" +
                "3.\t我校学生宿舍已由电信公司提供相应的电信服务。 \n" +
                "4.\t请家长和新生认真阅读《重庆邮电大学学生管理与学生自律协议书》，并按要求签名，在入学报到时交回各学院。 \n" +
                "5.\t请家长和新生认真阅读《致2016级新同学的一封信》，积极参加社会实践活动，认真观看新生入学教育视频，并在入学报到时将社会实践报告（如有图片、视频等资料请一并上交）和视频观后感交至各学院。 \n" +
                "6.\t新生入学报到，在途中请注意人身及财产安全，严防现金、证件、行李等财物被盗或遗失。在学校报到期间，千万不要委托他人代办入学报到手续，更不要把现金、存折、信用卡交给不相识的人，以免上当受骗。  \n" +
                "7.\t关于新生参加重庆市南岸区城乡合作医疗保险的说明：新生入学即可参加重庆市南岸区城乡合作医疗保险，城乡低保、农村五保、享受国家助学金大学生以及重度（一、二级）残疾大学生等困难学生参保需提供其困难证明的原件及复印件。参保学生个人缴费标准：10-120元/人/年，另外政府补助参保学生200～260元/人/年。具体的就诊和报销办法见入学体检时发放的《重庆邮电大学学生参加南岸区城乡合作医疗保险告知书》。住院可选有住院资格的校医院、区内定点医院、区外三甲医院，在区外住院的须选择公立医院，报销的最高限额14-16.8万元。\n"
               );

        SafetyInformation.setText("1、\t防止上当受骗。一些不法分子利用新生刚入学不熟悉的情况，以老师，学长或者老乡的身份骗取新生信任，然后以代缴学费、减免学费等多种方式进行诈骗。\n" +
                "2、\t不携带过多现金。数额较大的现金应该及时存入银行，存折、银行卡、身份证尽量分开放；使用银行卡要谨慎，以防密码泄露。\n" +
                "3、\t拒绝上门推销。许多不法分子以到寝室推销为名进行诈骗或盗窃，如若发现上门推销人员，应该及时报告宿管人员或者保卫处。\n" +
                "4、\t室内注意防盗。要保管好自己的笔记本电脑、手机等贵重物品，不要将其随意放置，以免被“顺手牵羊”。\n" +
                "5、\t注意消防安全。爱护消防设施，寝室内不违章使用大功率电器。\n" +
                "6、\t注意交通安全。不乘坐“黑车”和存在安全隐患的车辆\n" +
                "7、\t遇到情况及时与公安机关联系。在遇到不法侵害时，要及时与公安机关（110）或者学校保卫处联系（62461018,62460110）\n");

        ScholarshipInformation.setText("我校奖助学金面向全体学生，符合条件者均可申请。奖助学金种类多、金额高、覆盖面广（享受奖学金的学生约占总人数的30%），具体设置如下：\n" +
                "1.新生奖学金\n" +
                "（1）IT精英培养资助计划\n" +
                "入选对象：河北、河南、湖北、湖南、安徽、广西、四川、云南、贵州、重庆、陕西等11个省区当年参加普通高考的理工类考生。\n" +
                "入选条件：\n" +
                "①第一志愿报考我校并被我校录取的新生；\n" +
                "②高考成绩达到我校“IT精英培养资助计划”要求；\n" +
                "③身体健康、品学兼优、富有创新精神、有志于信息产业发展。\n" +
                "计划名额及资助标准：计划60名。获资助者进校后直接进入“IT精英班”学习，实行导师制，优先推荐免试硕士研究生，优先享受出国培训交流学习的机会等，学校资助其四年学费（具体按照《重庆邮电大学IT精英资助管理办法》执行）。\n" +
                "（2）优秀新生奖学金\n" +
                "参评对象：以第一志愿报考我校并以第一专业志愿录取的高考成绩优秀的新生。\n" +
                "评选比例及金额：按照各学院当年实际录取人数的2%评选，一次性奖励5000元/人。（与IT精英培养资助计划不重复享受，奖励金额就高不就低。）\n" +
                "2.综合奖学金\n" +
                "（1）国家奖学金：8000元/人·学年。\n" +
                "（2）国家励志奖学金：5000元/人·学年。\n" +
                "（3）优秀学生奖学金：500—5000元/人·学年。\n" +
                "（4）郭长波奖学金：5000元/人·学年。\n" +
                "（5）春华秋实奖学金：8000元/人·学年\n" +
                "3.企业奖学金\n" +
                "（1）长飞奖学金：8000元/人·学年。\n" +
                "（2）中天科技奖学金：3000元/人·学年。\n" +
                "（3）南都奖学金：3000元/人·学年。\n" +
                "（4）代小权奖学金：5000元/人·学年。\n" +
                "（5）四联川仪奖学金：2000元/人·学年。\n" +
                "（6）西山科技奖（助）学金：1000—10000元/人·学年。\n" +
                "（7）SK hynix创造人才奖学金：5000元/人·学年。\n" +
                "（8）通鼎奖学金：3000—4000元/人·学年。\n" +
                "4.单项奖学金\n" +
                "分为科技创新奖学金和文体艺术奖学金，根据学生参加科技和文体艺术竞赛获奖等级予以相应额度奖学金奖励（最高金额10000元/项·学年）。\n" +
                "5.助学金\n" +
                "国家助学金：2000—4000元/人·学年。\n" +
                "社会助学金：1000—2000元/人·学年。\n" +
                "\n");


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.table);
        //根据Bitmap对象创建ImageSpan对象
        ImageSpan imageSpan=new ImageSpan(getContext(),bitmap);
        //创建一个SpinnableString对象，以便插入ImageSpan对象封装的图像
        SpannableString spannableString=new SpannableString("一、绩点结算：\n" +
                "一门课程的学分绩点等于该课程的学分乘以绩点数，平均学分绩点等于学生取得的全部课程学分绩点之和除以这些课程学分数之和。\n" +
                "即：平均学分绩点=∑学分绩点/∑课程学分\n" +
                "学年（学期）课程学分累计绩点=∑学年（学期）所获课程学分绩点。\nreplace \n二、关于转专业：\n" +
                "1、\t学生在一般应在被录取的专业完成学业，有下列情况之一的可以允许转学或者转专业：\n" +
                "a、\t患有某种疾病或者生理缺陷（不含隐瞒病史入学者），经学校指定医院检查证明，确实不宜在本专业学习，但尚能在本校可以接收的专业或者其他学校别的专业学习的；\n" +
                "b、\t学校因为学科发展或人才需求变化，需要调整专业的；\n" +
                "c、\t学生按规定申请，经学校同意调整专业的。\n" +
                "2、\t转学、转专业手续按下列要求办理：\n" +
                "a、\t在本校范围内转专业，按照《重庆邮电大学本科生转专业管理办法》执行。\n" +
                "b、\t学生在本市内转学，需要本人提出申请，经本校与转入学校同意，本校申报重庆市教育委员会批准后办理转出手续；跨省转学，需本人提出申请，经本校于转入学校同意，重庆市教育委员会和转入地省级教育行政部门批准后，办理转出手续。\n" +
                "c、\t学生转入我校，其招生批次不得低于我在该生所在省（市）的招生批次。转入学生经我校接收学院考核和初审，报教务处审批后按教育部、重庆市教育委员会相关规定办理。\n" +
                "3、\t学生转学、转专业后，需修满转入专业培养方案规定的学分方可毕业。转学、转专业前已取得的学分的课程，由学生转入学院提出课程认定意见并报教务处批准。\n" +
                "4、\t有下列情况者，不得转学或者转专业：\n" +
                "a、\t入学未满一学期的；\n" +
                "b、\t由招生时所在地下一批次录取学校转入我校的；\n" +
                "c、\t由专科升入本科的（不含经专升本选拔考试录取的学生）；\n" +
                "d、\t本科三年级以上的；\n" +
                "e、\t应作退学处理的；\n" +
                "f、\t定向生未经单位同意的；\n" +
                "g、\t无正当理由的。\n" +
                "\n" +
                "三、\t关于毕业，结业与学位：\n" +
                "1、\t具有学籍的学生，修完培养方案规定的全部课程和实践教学环节，获得相应的学分，德育、体育全面合格，准予毕业，发给毕业证书。\n" +
                "学生同时修完第二专业规定课程，成绩合格，符合第二专业学业要求的，可获得第二专业学业证书。\n" +
                "2、\t学生有下列情况之一者，应作结业处理：\n" +
                "a、\t因课程或实践教学环节考核不合格者而未达到所在学院要求的；\n" +
                "b、\t因留校察看处分未取消的。\n" +
                "3、\t结业学生凡未取得学分的课程和教学环节，在学校规定学习年限内，允许结业后申请返校重修，重修合格并符合毕业条件者，可申请换发毕业证书，毕业时间按发证日起填写。逾期不申请重修或到规定期限考核仍不合格者，不再换发毕业证书。\n" +
                "4、\t学生在校学习未满一年发给学习证明，在校学习一年以上发给肄业证书。\n" +
                "5、\t经审查准予毕业的本科生，凡符合《重庆邮电大学学士学位授予工作细则》规定者，可向学校申请学士学位。\n" +
                "6、\t对违反国家招生规定入学者，学校不发给学历证书、学位证书；已发的学历证书、学位证书应予追回并交上级主管部门宣布证书无效。\n" +
                "7、\t毕业证、结业证、肄业证和学位证书遗失或损坏不能补发，可由学校出具证明书。证明书和原证书具有等同效力。\n");
        //用ImageSpan对象替换replace字符串
        spannableString.setSpan(imageSpan,120,127, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //将图像显示在TextView上
        HandbookInformation.setText(spannableString);
    }



}
