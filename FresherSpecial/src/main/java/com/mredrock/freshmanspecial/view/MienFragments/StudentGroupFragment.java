package com.mredrock.freshmanspecial.view.MienFragments;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mredrock.freshmanspecial.beans.MienBeans.GroupBean;
import com.mredrock.freshmanspecial.R;
import com.mredrock.freshmanspecial.units.MyRecyclerAdapter;
import com.mredrock.freshmanspecial.units.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxzhu on 2017/8/4.
 */

public class StudentGroupFragment extends BaseFragment {
    private RecyclerView tabs, list_group;
    private Fragment[] fragments = new Fragment[7];
    private MyRecyclerAdapter[] adapters = new MyRecyclerAdapter[7];
    @Override
    protected void initData() {
        tabs = $(R.id.tab_student_groups);
        list_group = $(R.id.list_groups);
//        initFragments();
        setTabs();
        setLeague();
        setRedRock();
        setStudentUnion();
        setSSTU();
        setMass();
        setVolunteer();
        setArt();
    }

    @Override
    protected int getResourceId() {
        return R.layout.special_2017_fragment_student_group;
    }

    private void setLeague() {
        List<GroupBean> list = new ArrayList<>();
        GroupBean bean1 = new GroupBean();
        bean1.setTitle("团委办公室");
        bean1.setContent("负责协调和承办团委的日常事务；负责团委大型会议的会务工作；团委相关工作的上传与下达；负责奖状、文件的保管工作；协助管理团委资产。");
        list.add(bean1);
        GroupBean bean2 = new GroupBean();
        bean2.setTitle("团委组织部");
        bean2.setContent("负责团的基层组织建设工作；开展推优入党工作；负责开展青年马克思主义者培养工程；指导主题团日活动的开展；开展五四评优表彰工作。");
        list.add(bean2);
        GroupBean bean3 = new GroupBean();
        bean3.setTitle("团委新媒体");
        bean3.setContent("负责团属刊物的制作；负责团委官方微博、微信等新媒体阵地的建设；负责myouth平台的建设管理；负责校内各项活动的采访、拍摄及相关专题视频的制作。");
        list.add(bean3);
        adapters[0] = new MyRecyclerAdapter(getActivity(),list,MyRecyclerAdapter.GROUP);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list_group.setLayoutManager(layoutManager);
        list_group.setAdapter(adapters[0]);
    }

    private void setRedRock() {
        List<GroupBean> list = new ArrayList<>();
        GroupBean bean1 = new GroupBean();
        bean1.setTitle("");
        bean1.setContent("重庆邮电大学红岩网校工作站是一个校园互联网开发团队，自2000年创立以来，已有17个年头了。它是一群志趣相投的在校大学生共同创建的，在校团委老师的指导下，学生自我管理，多年来走出了一大批优秀的IT人才。红岩网校工作站包括五个部门：产品策划及运营部，视觉设计部，Web研发部, 移动开发部和运维安全部。各个部门相互协作，共同推出了重邮小帮手微信公众号、掌上重邮、BT down铺、校园二手拾货网等一系列优秀的校园互联网产品，满足了广大师生的需求，获得一致好评。");
        list.add(bean1);
        GroupBean bean2 = new GroupBean();
        bean2.setTitle("产品策划及运营部");
        bean2.setContent("Ta以产品、宣传 、组织、赞助为核心方向，是挖掘用户需求，推广网站文化的运营师；Ta负责产品策划，原型设计，是创意的集结点，是项目的瞭望塔，也是网校各部门沟通的桥梁，是一名产品经理，；Ta负责网站线上线下活动的推广，收集并分析用户意见，提出产品功能升级方案，在网校，他们负责网站内容的发布和栏目的更新、网站专题及线上线下活动的策划和制作，利用新媒体平台宣传网校的产品和活动，负责与其他部门组织联系，进行网上的合作共建。我们需要耐心、细心与恒心，以及对产品的热情与责任心，需要学习Office、Ps、Ae、Axure等软件的操作，需要一个产品人的沟通能力和认真态度。");
        list.add(bean2);
        GroupBean bean3 = new GroupBean();
        bean3.setTitle("视觉设计部");
        bean3.setContent("Ta是设计产品图形用户界面，跟踪产品视觉及体验效果的设计师。Ta负责对网站进行整体创意和美术设计，移动端和PC端用户的界面设计；网页宣传海报设计；网站产品的动画及动效设计 ；对设计成果进行定期评估和研究，进行视觉元素的优化设计。部门的发展方向是视觉设计师和UI设计师。\n");
        list.add(bean3);
        GroupBean bean4 = new GroupBean();
        bean4.setTitle("Web研发部");
        bean4.setContent("Ta分为前端和后台方向。前端主要负责前端开发和页面制作，根据设计图用HTML和CSS完成页面制作；对网站前端性能做相应的优化。而后端是负责数据库设计和服务端逻辑开发；利用LNAMP搭建功能全面、操作方便的后台管理系统。在Web研发部学长学姐们的带领下，能让你感受到 HTML、CSS、JavaScript、PHP的深深的”善意”。在这里大家将会写代码，会上课，那么就会有作业。Web部里的骚年们上可搭建服务器，下可横扫pc移动h5。\n");
        list.add(bean4);
        GroupBean bean5 = new GroupBean();
        bean5.setTitle("移动开发部");
        bean5.setContent("Ta主要负责移动端产品的开发，以 Java&Kotlin，OC&Swift，C#为主要开发语言，涵盖Android，iOS，Windows 三个平台。在这里你可以学到App的开发制作。移动互联网就是我们的日常；从被大神虐哭到自己成为大神。在移动开发部你将对App开发感兴趣，对App制作有狂热的欲望。\n");
        list.add(bean5);
        GroupBean bean6 = new GroupBean();
        bean6.setTitle("运维安全部");
        bean6.setContent("Ta 既是红岩网校运维安全部(内部称之为 网校 SRE ( Site Reliability Engineering ))，也是重庆邮电大学 Linux 协会( CQUPT Linux User Group )。Ta 主要负责维护网校的服务器稳定和安全，同时也负责新项目的部署、环境安全配置和性能优化，还负责了校内的很多公益项目，例如 重庆邮电大学开源镜像站、重庆邮电大学 OpenStack 云计算资源池、重庆邮电大学 MineCraft/CounterStrike 服务器等等。在这里你可以学习到非常多的酷炫的 Linux 知识和信息安全知识，我们使用 Python 和 Go 语言来开发好玩有趣的项目，在这里你可以利用丰富的服务器资源来搭建自己想做的任何项目。在运维安全部，你要对技术执着、对高性能孜孜不倦，“如果不能优雅的解决这个问题，那我们就有了两个问题”。|´・ω・)ノ\n");
        list.add(bean6);
        adapters[1] = new MyRecyclerAdapter(getActivity(),list,MyRecyclerAdapter.GROUP);
    }

    private void setStudentUnion() {
        List<GroupBean> list = new ArrayList<>();
        GroupBean bean1 = new GroupBean();
        bean1.setTitle("");
        bean1.setContent("重庆邮电大学学生会是由校党委、市学联领导，由校团委具体指导的群众性组织。重庆邮电大学学生会以“全心全意为同学服务”为宗旨，充分发挥同学主人翁精神，加强学风建设，营造浓厚的读书氛围，以提高学生综合素质、推进校园文化建设为目标，创建了一批深受同学们喜爱的科技文化活动，积极营造校园爱心氛围，结合校园实际，积极加强大学生法制观念，维护大学生合法权益，开展文体活动，丰富校园文化生活。利用现有资源，打造网络交流平台，通过编撰刊物，展示理论成果。");
        list.add(bean1);
        GroupBean bean2 = new GroupBean();
        bean2.setTitle("主席团");
        bean2.setContent("全面主持学生会日常工作，督促和指导各部门工作的开展； 统筹协调校院两级学生会开展工作；定期向学校领导、学生委员会、学生代表大会汇报工作；学生会副主席协助主席开展工作，分管相应工作。");
        list.add(bean2);
        GroupBean bean3 = new GroupBean();
        bean3.setTitle("综合部");
        bean3.setContent("负责学生会内部日常管理；协调各学院学生会工作；负责与兄弟高校学生会及校外商家的联络；牵头对各学院学生会的考核。\n");
        list.add(bean3);
        GroupBean bean4 = new GroupBean();
        bean4.setTitle("学习部");
        bean4.setContent("推进学风建设，搭建师生交流桥梁；组织开展辩论赛、读写行等活动； 建设“朋辈辅导”互助平台。");
        list.add(bean4);
        GroupBean bean5 = new GroupBean();
        bean5.setTitle("宣传部");
        bean5.setContent("利用新媒体开展学生会宣传工作；设计制作视频、微电影、海报等文化产品；开展校园文化创意活动。");
        list.add(bean5);
        GroupBean bean6 = new GroupBean();
        bean6.setTitle("权益提案部");
        bean6.setContent("处理提案工作委员会日常事务；负责学生代表提案的征集、立案、报送、督办等工作；搭建青年学生维权服务平台。");
        list.add(bean6);
        GroupBean bean7 = new GroupBean();
        bean7.setTitle("生活服务部");
        bean7.setContent("处理食品安全与伙食监督管理委员会日常事务；做好与基建后勤、宿舍管理部门的沟通联系；组织开展健康生活、文明生活主题活动");
        list.add(bean7);
        GroupBean bean8 = new GroupBean();
        bean8.setTitle("文艺部");
        bean8.setContent("开展校内文艺活动；搭建同学才艺展示平台；培养同学文艺爱好。");
        list.add(bean8);
        GroupBean bean9 = new GroupBean();
        bean9.setTitle("体育部");
        bean9.setContent("组织开展“走下网络、走出宿舍、走向操场”群众性体育锻 炼活动。");
        list.add(bean9);
        GroupBean bean10 = new GroupBean();
        bean10.setTitle("女生部");
        bean10.setContent("反映广大女生的合理利益诉求；组织开展女生节系列活动；配合“文峰女子学堂”开展各类活动。");
        list.add(bean10);
        adapters[2] = new MyRecyclerAdapter(getActivity(),list,MyRecyclerAdapter.GROUP);
    }

    private void setSSTU() {
        List<GroupBean> list = new ArrayList<>();
        GroupBean bean1 = new GroupBean();
        bean1.setTitle("");
        bean1.setContent("重庆邮电大学学生科技联合会（SSTU：Student  Science  and  Technology  Union）简称学生科联，是团委指导下，以服务学生创新创业为职责的校级学生组织。学生科联以“创新、高效、团结、求实”为宗旨，以“成功与科技相辉映，科联携你我共腾飞”为口号，以“挑战杯”、“创青春”竞赛为龙头，以“学生科技文化节”、“文峰青年大讲堂”、“创新创业训练营”、“重邮青年说”、“学长演播厅”、“无线电猎狐大赛”等活动为品牌，积极参与学校创新创业教育和实践工作。");
        list.add(bean1);
        GroupBean bean2 = new GroupBean();
        bean2.setTitle("主席团");
        bean2.setContent("全面负责科联各项工作的开展；制定科联年度工作计划和发展规划；指导、检查、督促各部门开展工作，协调各方面关系，定期向团委汇报科联的工作情况。");
        list.add(bean2);
        GroupBean bean3 = new GroupBean();
        bean3.setTitle("综合部");
        bean3.setContent("负责学生科联日常事务的管理；组织开展学生科联内部活动；协调各学院学生科协的工作；负责收集和发布校内外科创赛事资讯；负责与兄弟高校及校外商家的联络。");
        list.add(bean3);
        GroupBean bean4 = new GroupBean();
        bean4.setTitle("科创竞赛部");
        bean4.setContent("负责开展“科普先锋秀”、“无线电猎狐大赛”等活动；组织“挑战杯”大学生课外学术科技作品竞赛、“创青春”全国大学生创业大赛的申报和立项工作。");
        list.add(bean4);
        GroupBean bean5 = new GroupBean();
        bean5.setTitle("项目管理部");
        bean5.setContent("组织开展学生科联的创新、创业活动。负责协调开展学生科技节系列活动；负责组织“大学生创新创业训练营”以及“创新创业高端论坛”等活动。");
        list.add(bean5);
        GroupBean bean6 = new GroupBean();
        bean6.setTitle("科技人文部");
        bean6.setContent("组织开展“重邮青年说”、“学长演播厅”等校级品牌活动；负责举办“文峰青年大讲堂”大型高端讲座；指导学生科协系统的创新文化建设工作。");
        list.add(bean6);
        GroupBean bean7 = new GroupBean();
        bean7.setTitle("信息部");
        bean7.setContent("主要负责Html5网页的开发、UI设计、三创网的宣传推广和科技创新实践活动的策划与开展，为科联的活动和宣传提供技术支持。");
        list.add(bean7);
        GroupBean bean8 = new GroupBean();
        bean8.setTitle("媒体运营部");
        bean8.setContent("主要负责海报、活动视频、微电影以及动画的的设计与制作；负责科联公众平台的线上运营和网络宣传推送等；在为科联活动提供创新创意的同时创造多样化创新性活动宣传。");
        list.add(bean8);
        adapters[3] = new MyRecyclerAdapter(getActivity(),list,MyRecyclerAdapter.GROUP);
    }

    private void setMass() {
        List<GroupBean> list = new ArrayList<>();
        GroupBean bean1 = new GroupBean();
        bean1.setTitle("");
        bean1.setContent("社团联合会在校团委的直接指导下，管理社团工作、服务社团发展的学生组织，旗下管辖着涵盖文学学术、实践服务、艺术\n" +
                "文化、爱好兴趣、体育运动等功能丰富、类型兼备的各类学生社团组织。它以充分调动众多社团及其会员的积极性和创造性，\n" +
                "全面开展有深度、有内涵、有品位、有价值、有意义的社团活动，不断丰富校园文化生活，提高当代大学生的学习能力、实\n" +
                "践能力、组织能力和创新能力为目标。 作为青年自组织，学生社团是培养和发展大学生兴趣爱好的广阔平台，是培养学生自\n" +
                "我教育、自我管理、自我服务的有效形式，是构建“一体两翼”学校团建大格局的重要组成部分，是凝聚青年、丰富校园文化\n" +
                "、培育和传承大学精神的重要载体，目前，能否建设好高校的社团已经成为一个高校是否具有活力和文化氛围的重要标志之\n" +
                "一。学生社团遍布全国高校校园的各个角落，扮演着丰富校园文化生活、提升校园文化品位、引领校园文化时尚的重要角色。\n" +
                "社团联合会扮演着对各社团服务和管理的双重角色，对学生社团的发展起着至关重要的作用 ");
        list.add(bean1);
        GroupBean bean2 = new GroupBean();
        bean2.setTitle("主席团");
        bean2.setContent("全面主持学生社联各项工作；制定社联工作计划和发展规划；指导、检查、督促各社团开展工作，协调与各学生组织 和社团之间的关系。\n");
        list.add(bean2);
        GroupBean bean3 = new GroupBean();
        bean3.setTitle("综合部");
        bean3.setContent("社联小当家。负责社联内部文书、考勤、活动筹备等工作，策划组织各项内部活动；负责学生社团精品活动物资的审批发放、场地申请；同时负责与兄弟高校社联组织、校内各组织及校外商家的联络。");
        list.add(bean3);
        GroupBean bean4 = new GroupBean();
        bean4.setTitle("宣传部");
        bean4.setContent("活动先行者。负责社联新媒体运营工作，搭建学生社团宣传平台。负责设计社联活动海报、视频等文化产品，同时审批社团活动海报等宣传制品。");
        list.add(bean4);
        GroupBean bean5 = new GroupBean();
        bean5.setTitle("社团服务部");
        bean5.setContent("社团小帮手。负责管理社团，是社联连接社团、社团部的桥梁，组织社团的招新、运行、换届以及社团评奖评优等工作，带动社团的发展。");
        list.add(bean5);
        GroupBean bean6 = new GroupBean();
        bean6.setTitle("社团活动部");
        bean6.setContent("活动执行官。负责审核、指导学生社团开展精品活动，牵头社联各部门举办社团达人秀、社团推广暨展示季等大型活动，增添校园文化色彩。");
        list.add(bean6);
        adapters[4] = new MyRecyclerAdapter(getActivity(),list,MyRecyclerAdapter.GROUP);
    }

    private void setVolunteer() {
        List<GroupBean> list = new ArrayList<>();
        GroupBean bean1 = new GroupBean();
        bean1.setTitle("");
        bean1.setContent("重庆邮电大学青年志愿者协会成立于2014年6月，由校团委指导，秉承“互相帮助、助人自助、无私奉献、不求回报”的志愿者精神，有效的组织全校志愿者参与校内外志愿服务，同时负责学校社会实践工作和志愿服务活动的协调。组织开展全校“三下乡”社会实践活动、“12.5国际志愿者日”志愿周系列活动、“3•5学雷锋月”系列活动、“重庆邮电大学优秀志愿项目答辩”、“校内志愿服务时长管理”、“市民学校”志愿活动等相关工作。\n" +
                "重庆邮电大学青年志愿者协会的组织结构为：");
        list.add(bean1);
        GroupBean bean2 = new GroupBean();
        bean2.setTitle("主席团");
        bean2.setContent("统筹协调校院两级青年志愿者组织开展工作；拓展对外交流合作平台。");
        list.add(bean2);
        GroupBean bean3 = new GroupBean();
        bean3.setTitle("综合管理部");
        bean3.setContent("负责青协日常事务统筹和物资管理；负责学校各部门和组织的时长认定与监管；完善与各学院，以及其他学校青协的联系与合作。");
        list.add(bean3);
        GroupBean bean4 = new GroupBean();
        bean4.setTitle("青年志愿者服务总队");
        bean4.setContent("负责各类志愿服务活动组织及志愿者招募工作；负责优秀志愿项目的评选工作。");
        list.add(bean4);
        GroupBean bean5 = new GroupBean();
        bean5.setTitle("实践服务部");
        bean5.setContent("负责暑期“三下乡”社会实践活动的组织工作；统筹城乡社区市民学校建设工作。");
        list.add(bean5);
        GroupBean bean6 = new GroupBean();
        bean6.setTitle("宣传推广部");
        bean6.setContent("负责青年志愿服务活动宣传；负责校内外志愿活动的跟踪纪录；对志愿服务品牌项目进行推广；帮助建设好更完善的志愿服体系。");
        list.add(bean6);
        adapters[5] = new MyRecyclerAdapter(getActivity(),list,MyRecyclerAdapter.GROUP);
    }

    private void setArt() {
        List<GroupBean> list = new ArrayList<>();
        GroupBean bean1 = new GroupBean();
        bean1.setTitle("");
        bean1.setContent("重庆邮电大学大学生艺术团是在校团委直接指导管理下的学生艺术团体，肩负组织学校重大文艺活动，代表学校对外联谊，\n" +
                "演出和参加省市文艺大赛的重任。秉承“服务同学，锻炼自我”的宗旨，以丰富校园文化生活，陶冶情操，加强与其他院校艺\n" +
                "术交流与沟通，为校宣传争光，活跃人文气氛等为前提，要求每个成员在艺术实践中以高标准、严要求的态度约束自己。 大\n" +
                "学生艺术团由管乐团、合唱团、舞蹈团、曲艺团组成。主要承担着我校各项大型演出任务，组织编排各项文艺节目，为有文\n" +
                "艺特长的同学搭建良好的交流和展示平台，每个分团都有固定的训练时间和专业指导老师。近年来，大学生艺术团一次次圆\n" +
                "满完成学校交给的各项文艺演出任务，并锻炼和培养了一批批艺术人才。为我校大学生文艺素质的培养，树立了一面旗帜。");
        list.add(bean1);
        GroupBean bean2 = new GroupBean();
        bean2.setTitle("主席团");
        bean2.setContent("全面主持艺术团各项工作；负责艺术团工作计划的制定和艺术团建设规划；负责艺术团分团干部的培养、选拔工作；负责策划\n" +
                "艺术团各类演出活动。");
        list.add(bean2);
        GroupBean bean3 = new GroupBean();
        bean3.setTitle("综合部");
        bean3.setContent("负责艺术团日常事务管理；各类文艺活动的后勤保障工作；负责学生活动中心、管乐团排练厅场地管理；协调和联系其他学生组织及各分团间的工作；负责礼仪志愿者的日常训练及成员的考核工作。");
        list.add(bean3);
        GroupBean bean4 = new GroupBean();
        bean4.setTitle("管乐团");
        bean4.setContent("负责管乐团的日常训练及管乐团成员的考核、招新工作；组织参加校内外的演出或比赛； 协助指导教师做好管乐团的节目编排工作。");
        list.add(bean4);
        GroupBean bean5 = new GroupBean();
        bean5.setTitle("舞蹈团");
        bean5.setContent("负责舞蹈团的日常训练及舞蹈团成员的考核、招新工作；组织参加校内外的演出或比赛；协助指导教师做好舞蹈团的节目编排工作。");
        list.add(bean5);
        GroupBean bean6 = new GroupBean();
        bean6.setTitle("民乐团");
        bean6.setContent("负责民乐团的日常训练及民乐团成员的考核、招新工作；组织参加校内外的演出或比赛；协助指导教师做好民乐团的节目编排工作。");
        list.add(bean6);
        GroupBean bean7 = new GroupBean();
        bean7.setTitle("合唱团");
        bean7.setContent("负责合唱团的日常训练及合唱团成员的考核、招新工作；组织参加校内外的演出或比赛；协助指导教师做好合唱团的节目编排工作。");
        list.add(bean7);
        GroupBean bean8 = new GroupBean();
        bean8.setTitle("话剧团");
        bean8.setContent("负责话剧团的日常训练及话剧团成员的考核、招新工作；组织参加校内外的演出或比赛；创作并编排新话剧剧本等。");
        list.add(bean8);
        adapters[6] = new MyRecyclerAdapter(getActivity(),list,MyRecyclerAdapter.GROUP);
    }

    private void initFragments() {
//        int wide = ScreenUnit.bulid(this).getDpWide()*2/7;
//        tabLayout.setMinimumWidth(wide);

        fragments[0] = new GroupContentFragment("重庆邮电大学团委各部室\n" +
                "\n"+
                "【团委办公室】\n" +
                "负责协调和承办团委的日常事务；负责团委大型会议的会务工作；团委相关工作的上传与下达；负责奖状、文件的保管工作；协助管理团委资产。\n" +
                "【团委组织部】\n" +
                "负责团的基层组织建设工作；开展推优入党工作；负责开展青年马克思主义者培养工程； 指导主题团日活动的开展；开展五\n" +
                "四评优表彰工作。\n" +
                "【团委宣传部】\n" +
                "负责团属刊物的制作；负责团委官方微博、微信等新媒体阵地的建设；负责myouth平台的建设管理；负责校内各项活动的采\n" +
                "访、拍摄及相关专题视频的制作。\n");
        fragments[1] = new GroupContentFragment("红岩网校\n" +
                "\n"+
                "重庆邮电大学红岩网校工作站是一个校园互联网开发团队，自2000年创立以来，已有17个年头了。它是一群志趣相投的在校大学生共同创建的，在校团委老师的指导下，学生自我管理，多年来走出了一大批优秀的IT人才。红岩网校工作站包括五个部门：产品策划及运营部，视觉设计部，Web研发部, 移动开发部和运维安全部。各个部门相互协作，共同推出了重邮小帮手微信公众号、掌上重邮、BT down铺、校园二手拾货网等一系列优秀的校园互联网产品，满足了广大师生的需求，获得一致好评。\n" +
                "\n" +
                "【产品策划及运营部】Ta以产品、宣传 、组织、赞助为核心方向，是挖掘用户需求，推广网站文化的运营师；Ta负责产品策划，原型设计，是创意的集结点，是项目的瞭望塔，也是网校各部门沟通的桥梁，是一名产品经理，；Ta负责网站线上线下活动的推广，收集并分析用户意见，提出产品功能升级方案，在网校，他们负责网站内容的发布和栏目的更新、网站专题及线上线下活动的策划和制作，利用新媒体平台宣传网校的产品和活动，负责与其他部门组织联系，进行网上的合作共建。我们需要耐心、细心与恒心，以及对产品的热情与责任心，需要学习Office、Ps、Ae、Axure等软件的操作，需要一个产品人的沟通能力和认真态度。\n" +
                "\n" +
                "【视觉设计部】\tTa是设计产品图形用户界面，跟踪产品视觉及体验效果的设计师。Ta负责对网站进行整体创意和美术设计，移动端和PC端用户的界面设计；网页宣传海报设计；网站产品的动画及动效设计 ；对设计成果进行定期评估和研究，进行视觉元素的优化设计。部门的发展方向是视觉设计师和UI设计师。\n" +
                "\n" +
                "【Web研发部】\tTa分为前端和后台方向。前端主要负责前端开发和页面制作，根据设计图用HTML和CSS完成页面制作；对网站前端性能做相应的优化。而后端是负责数据库设计和服务端逻辑开发；利用LNAMP搭建功能全面、操作方便的后台管理系统。在Web研发部学长学姐们的带领下，能让你感受到 HTML、CSS、JavaScript、PHP的深深的”善意”。在这里大家将会写代码，会上课，那么就会有作业。Web部里的骚年们上可搭建服务器，下可横扫pc移动h5。\n" +
                "\n" +
                "【移动开发部】Ta主要负责移动端产品的开发，以 Java&Kotlin，OC&Swift，C#为主要开发语言，涵盖Android，iOS，Windows 三个平台。在这里你可以学到App的开发制作。移动互联网就是我们的日常；从被大神虐哭到自己成为大神。在移动开发部你将对App开发感兴趣，对App制作有狂热的欲望。\n" +
                "【运维安全部】  Ta 既是红岩网校运维安全部(内部称之为 网校 SRE ( Site Reliability Engineering ))，也是重庆邮电大学 Linux 协会( CQUPT Linux User Group )。Ta 主要负责维护网校的服务器稳定和安全，同时也负责新项目的部署、环境安全配置和性能优化，还负责了校内的很多公益项目，例如 重庆邮电大学开源镜像站、重庆邮电大学 OpenStack 云计算资源池、重庆邮电大学 MineCraft/CounterStrike 服务器等等。在这里你可以学习到非常多的酷炫的 Linux 知识和信息安全知识，我们使用 Python 和 Go 语言来开发好玩有趣的项目，在这里你可以利用丰富的服务器资源来搭建自己想做的任何项目。在运维安全部，你要对技术执着、对高性能孜孜不倦，“如果不能优雅的解决这个问题，那我们就有了两个问题”。|´・ω・)ノ\n");
        fragments[2] = new GroupContentFragment("重庆邮电大学学生科技联合会:\n" +
                "\n"+
                "重庆邮电大学学生科技联合会（SSTU：Student  Science  and  Technology  Union）简称学生科联，是团委指导下，以服务学生创新创业为职责的校级学生组织。学生科联以“创新、高效、团结、求实”为宗旨，以“成功与科技相辉映，科联携你我共腾飞”为口号，以“挑战杯”、“创青春”竞赛为龙头，以“学生科技文化节”、“文峰青年大讲堂”、“创新创业训练营”、“重邮青年说”、“学长演播厅”、“无线电猎狐大赛”等活动为品牌，积极参与学校创新创业教育和实践工作。\n" +
                "【主席团】\n" +
                "全面负责科联各项工作的开展；制定科联年度工作计划和发展规划；指导、检查、督促各部门开展工作，协调各方面关系，定期向团委汇报科联的工作情况。\n" +
                "【综合部】\n" +
                "负责学生科联日常事务的管理；组织开展学生科联内部活动；协调各学院学生科协的工作；负责收集和发布校内外科创赛事资讯；负责与兄弟高校及校外商家的联络。\n" +
                "【科创竞赛部】\n" +
                "负责开展“科普先锋秀”、“无线电猎狐大赛”等活动；组织“挑战杯”大学生课外学术科技作品竞赛、“创青春”全国大学生创业大赛的申报和立项工作。\n" +
                "【项目管理部】\n" +
                "组织开展学生科联的创新、创业活动。负责协调开展学生科技节系列活动；负责组织“大学生创新创业训练营”以及“创新创业高端论坛”等活动。\n" +
                "【科技人文部】\n" +
                "组织开展“重邮青年说”、“学长演播厅”等校级品牌活动；负责举办“文峰青年大讲堂”大型高端讲座；指导学生科协系统的创新文化建设工作。\n" +
                "【信息部】\n" +
                "主要负责Html5网页的开发、UI设计、三创网的宣传推广和科技创新实践活动的策划与开展，为科联的活动和宣传提供技术支持。\n" +
                "【媒体运营部】\n" +
                "主要负责海报、活动视频、微电影以及动画的的设计与制作；负责科联公众平台的线上运营和网络宣传推送等；在为科联活动提供创新创意的同时创造多样化创新性活动宣传。 \n");
        fragments[3] = new GroupContentFragment("重庆邮电大学青年志愿者协会\n" +
                "\n"+
                "重庆邮电大学青年志愿者协会成立于2014年6月，由校团委指导，秉承“互相帮助、助人自助、无私奉献、不求回报”的志愿者精神，有效的组织全校志愿者参与校内外志愿服务，同时负责学校社会实践工作和志愿服务活动的协调。组织开展全校“三下乡”社会实践活动、“12.5国际志愿者日”志愿周系列活动、“3•5学雷锋月”系列活动、“重庆邮电大学优秀志愿项目答辩”、“校内志愿服务时长管理”、“市民学校”志愿活动等相关工作。\n" +
                "重庆邮电大学青年志愿者协会的组织结构为：\n" +
                "【主席团】\n" +
                "统筹协调校院两级青年志愿者组织开展工作；拓展对外交流合作平台。\n" +
                "【综合管理部】\n" +
                "负责青协日常事务统筹和物资管理；负责学校各部门和组织的时长认定与监管；完善与各学院，以及其他学校青协的联系与合作。\n" +
                "【青年志愿者服务总队】\n" +
                "负责各类志愿服务活动组织及志愿者招募工作；负责优秀志愿项目的评选工作。\n" +
                "【实践服务部】\n" +
                "负责暑期“三下乡”社会实践活动的组织工作；统筹城乡社区市民学校建设工作。\n" +
                "【宣传推广部】\n" +
                "负责青年志愿服务活动宣传；负责校内外志愿活动的跟踪纪录；对志愿服务品牌项目进行推广；帮助建设好更完善的志愿服体系。 \n");
        fragments[4] = new GroupContentFragment("重庆邮电大学学生会:\n" +
                "\n"+
                "重庆邮电大学学生会是由校党委、市学联领导，由校团委具体指导的群众性组织。重庆邮电大学学生会以“全心全意为同学服\n" +
                "务”为宗旨，充分发挥同学主人翁精神，加强学风建设，营造浓厚的读书氛围，以提高学生综合素质、推进校园文化建设为目\n" +
                "标，创建了一批深受同学们喜爱的科技文化活动，积极营造校园爱心氛围，结合校园实际，积极加强大学生法制观念，维护大\n" +
                "学生合法权益，开展文体活动，丰富校园文化生活。利用现有资源，打造网络交流平台，通过编撰刊物，展示理论成果。\n" +
                "【主席团】\n" +
                "全面主持学生会日常工作，督促和指导各部门工作的开展； 统筹协调校院两级学生会开展工作；定期向学校领导、学生委员\n" +
                "会、学生代表大会汇报工作；学生会副主席协助主席开展工作，分管相应工作。\n" +
                "【综合部】\n" +
                "负责学生会内部日常管理；协调各学院学生会工作；负责与兄弟高校学生会及校外商家的联络；牵头对各学院学生会的考核。\n" +
                "【学习部】\n" +
                "推进学风建设，搭建师生交流桥梁；组织开展辩论赛、读写行等活动； 建设“朋辈辅导”互助平台。\n" +
                "【宣传部】\n" +
                "利用新媒体开展学生会宣传工作；设计制作视频、微电影、海报等文化产品；开展校园文化创意活动。\n" +
                "【权益提案部】\n" +
                "处理提案工作委员会日常事务；负责学生代表提案的征集、立案、报送、督办等工作；搭建青年学生维权服务平台。\n" +
                "【生活服务部】\n" +
                "处理食品安全与伙食监督管理委员会日常事务；做好与基建后勤、宿舍管理部门的沟通联系；组织开展健康生活、文明生活主\n" +
                "题活动。\n" +
                "【文艺部】\n" +
                "开展校内文艺活动；搭建同学才艺展示平台；培养同学文艺爱好。\n" +
                "【体育部】\n" +
                "组织开展“走下网络、走出宿舍、走向操场”群众性体育锻 炼活动。\n" +
                "【女生部】\n" +
                "反映广大女生的合理利益诉求；组织开展女生节系列活动；配合“文峰女子学堂”开展各类活动。 \n");
        fragments[5] = new GroupContentFragment("重庆邮电大学大学生艺术团\n" +
                "\n"+
                "重庆邮电大学大学生艺术团是在校团委直接指导管理下的学生艺术团体，肩负组织学校重大文艺活动，代表学校对外联谊，\n" +
                "演出和参加省市文艺大赛的重任。秉承“服务同学，锻炼自我”的宗旨，以丰富校园文化生活，陶冶情操，加强与其他院校艺\n" +
                "术交流与沟通，为校宣传争光，活跃人文气氛等为前提，要求每个成员在艺术实践中以高标准、严要求的态度约束自己。 大\n" +
                "学生艺术团由管乐团、合唱团、舞蹈团、曲艺团组成。主要承担着我校各项大型演出任务，组织编排各项文艺节目，为有文\n" +
                "艺特长的同学搭建良好的交流和展示平台，每个分团都有固定的训练时间和专业指导老师。近年来，大学生艺术团一次次圆\n" +
                "满完成学校交给的各项文艺演出任务，并锻炼和培养了一批批艺术人才。为我校大学生文艺素质的培养，树立了一面旗帜。\n" +
                "【主席团】\n" +
                "全面主持艺术团各项工作；负责艺术团工作计划的制定和艺术团建设规划；负责艺术团分团干部的培养、选拔工作；负责策划\n" +
                "艺术团各类演出活动。\n" +
                "【综合部】\n" +
                "负责艺术团日常事务管理；各类文艺活动的后勤保障工作；负责学生活动中心、管乐团排练厅场地管理；协调和联系其他学生\n" +
                "组织及各分团间的工作；负责礼仪志愿者的日常训练及成员的考核工作。\n" +
                "【管乐团】\n" +
                "负责管乐团的日常训练及管乐团成员的考核、招新工作；组织参加校内外的演出或比赛； 协助指导教师做好管乐团的节目编\n" +
                "排工作。\n" +
                "【舞蹈团】\n" +
                "负责舞蹈团的日常训练及舞蹈团成员的考核、招新工作；组织参加校内外的演出或比赛； 协助指导教师做好舞蹈团的节目编\n" +
                "排工作。\n" +
                "【民乐团】\n" +
                "负责民乐团的日常训练及民乐团成员的考核、招新工作；组织参加校内外的演出或比赛； 协助指导教师做好民乐团的节目编\n" +
                "排工作。\n" +
                "【合唱团】\n" +
                "负责合唱团的日常训练及合唱团成员的考核、招新工作；组织参加校内外的演出或比赛； 协助指导教师做好合唱团的节目编\n" +
                "排工作。\n" +
                "【话剧团】\n" +
                "负责话剧团的日常训练及话剧团成员的考核、招新工作；组织参加校内外的演出或比赛；创作并编排新话剧剧本等。\n" );
        fragments[6] = new GroupContentFragment("重庆邮电大学大学生艺术团\n" +
                "\n"+
                "重庆邮电大学大学生艺术团是在校团委直接指导管理下的学生艺术团体，肩负组织学校重大文艺活动，代表学校对外联谊，\n" +
                "演出和参加省市文艺大赛的重任。秉承“服务同学，锻炼自我”的宗旨，以丰富校园文化生活，陶冶情操，加强与其他院校艺\n" +
                "术交流与沟通，为校宣传争光，活跃人文气氛等为前提，要求每个成员在艺术实践中以高标准、严要求的态度约束自己。 大\n" +
                "学生艺术团由管乐团、合唱团、舞蹈团、曲艺团组成。主要承担着我校各项大型演出任务，组织编排各项文艺节目，为有文\n" +
                "艺特长的同学搭建良好的交流和展示平台，每个分团都有固定的训练时间和专业指导老师。近年来，大学生艺术团一次次圆\n" +
                "满完成学校交给的各项文艺演出任务，并锻炼和培养了一批批艺术人才。为我校大学生文艺素质的培养，树立了一面旗帜。\n" +
                "【主席团】\n" +
                "全面主持艺术团各项工作；负责艺术团工作计划的制定和艺术团建设规划；负责艺术团分团干部的培养、选拔工作；负责策划\n" +
                "艺术团各类演出活动。\n" +
                "【综合部】\n" +
                "负责艺术团日常事务管理；各类文艺活动的后勤保障工作；负责学生活动中心、管乐团排练厅场地管理；协调和联系其他学生\n" +
                "组织及各分团间的工作；负责礼仪志愿者的日常训练及成员的考核工作。\n" +
                "【管乐团】\n" +
                "负责管乐团的日常训练及管乐团成员的考核、招新工作；组织参加校内外的演出或比赛； 协助指导教师做好管乐团的节目编\n" +
                "排工作。\n" +
                "【舞蹈团】\n" +
                "负责舞蹈团的日常训练及舞蹈团成员的考核、招新工作；组织参加校内外的演出或比赛； 协助指导教师做好舞蹈团的节目编\n" +
                "排工作。\n" +
                "【民乐团】\n" +
                "负责民乐团的日常训练及民乐团成员的考核、招新工作；组织参加校内外的演出或比赛； 协助指导教师做好民乐团的节目编\n" +
                "排工作。\n" +
                "【合唱团】\n" +
                "负责合唱团的日常训练及合唱团成员的考核、招新工作；组织参加校内外的演出或比赛； 协助指导教师做好合唱团的节目编\n" +
                "排工作。\n" +
                "【话剧团】\n" +
                "负责话剧团的日常训练及话剧团成员的考核、招新工作；组织参加校内外的演出或比赛；创作并编排新话剧剧本等。\n" );
    }

    private void setTabs(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tabs.setLayoutManager(manager);
        final List<String> list = new ArrayList<>();
        list.add("团委部门");
        list.add("红岩");
        list.add("校学生会");
        list.add("科联");
        list.add("社联");
        list.add("校青协");
        list.add("大艺团");
        final MyRecyclerAdapter adapter = new MyRecyclerAdapter(getActivity(),list,MyRecyclerAdapter.TABS);
        adapter.setOnTabClickListener(new MyRecyclerAdapter.OnTabClickListener() {
            private boolean firstClick = true;
            View lastView = null;
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTabClickListener(int position, View view) {
                if (firstClick) {
                    lastView = adapter.getFirstView();
                    firstClick = false;
                }
                if (lastView != null) {
                    lastView.setBackground(null);
                }
                lastView = view;
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                list_group.setLayoutManager(layoutManager);
                list_group.setAdapter(adapters[position]);
            }
        });
        tabs.setAdapter(adapter);
    }
}
