package com.hitsz.eatut;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.hitsz.eatut.adapter.order;
import com.hitsz.eatut.database.CanteenInfo;
import com.hitsz.eatut.database.Comment;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.UserInfo;
import com.hitsz.eatut.database.WindowInfo;
import com.hitsz.eatut.database.orderFood;
import com.hitsz.eatut.adapter.dish;
import com.hankcs.hanlp.HanLP;

import org.litepal.LitePal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class BaseClass {
    /**
     * @author  lixiang
     * 读取输入的手机号或学号，判断SharedPreferences中是否有此用户名
     */
    public static boolean isUserExist(String phoneNumber){
        List<UserInfo> users_phone = LitePal.where("telephoneNumber = ?", phoneNumber).find(UserInfo.class);
        return (!users_phone.isEmpty());
    }

    /**
     * @author  lixiang
     * 判断输入是否合法
     */
    public static boolean isUserNameValid(String userName) {
        return userName.length() >= 2;
    }

    public static boolean isStudentNumberValid(String studentNumber) {
        return studentNumber.length() == 9;
    }

    public static boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }


    /**
     * @author  lixiang
     * 评论分析： HanLP中的TextRank关键词提取
     */
    public static String getCommentKeyword(String comments){
        List<String> keywordList = HanLP.extractKeyword(comments, 5);
        String keywords = "";
        for (String keyword: keywordList){
            keywords = keywords.concat(keyword+' ');
        }
        return keywords;
    }

    /**
     * @author  lixiang
     * 从食堂的所有commnets中提取关键词
     */
    public static String getCanteenCommentKeyword(String canteen){
        List<Comment> comments = LitePal.where("commentCanteen = ?", canteen).find(Comment.class);
        String allComment = "";
        for (Comment comments1: comments){
            allComment = allComment.concat(comments1.getCommentText());
        }
        return getCommentKeyword(allComment);
    }

    /**
     * MD5码加密用户密码
     * @author Lixiang
     */
    @NonNull
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @author lixiang
     * -------------------------------------------树部分---------------------------------------------
     */
//存储结点信息
    public static class NodeElement{
        String nodeName;        //名称（食堂、档口、菜品）
        float score;              //单结点评分
        float sumScore;           //累加评分
        int childNumber;        //下属级别数量
        int idInDatabase;       //所在数据库中的id
    }

    //左孩子右兄弟表示法存储树的结构
    public static class TreeNode{
        NodeElement nodeData;   //结点数据
        TreeNode firstChild;    //第一个孩子
        TreeNode firstBrother;  //第一个兄弟
        private TreeNode(NodeElement nodeData){
            this.nodeData = nodeData;
        }
    }

    public static TreeNode rootNode;

//-------------------------------建树------------------------------
    /**
     * 函数功能 从数据库读取食堂、档口、菜品数据存入结点信息(初始化树）
     * @author lixiang
     */
//建立一棵树～
    public static void buildTreeFromDatabase(){
        buildCanteenNodesFromDatabase();
        buildWindowNodesFromDatabase();
        buildDishNodesFromDatabase();
    }

    //从父结点开始找到子结点末尾添加结点
    private static void addNewNodeFromParentNode(TreeNode tempNode, TreeNode newNode){
        tempNode.nodeData.childNumber ++;//父结点的子结点数++
        if (tempNode.firstChild == null){
            tempNode.firstChild = newNode;
        }
        else {//父结点下已经有子结点，到最后添加
            tempNode = tempNode.firstChild;
            while(tempNode.firstBrother != null){
                tempNode = tempNode.firstBrother;
            }
            tempNode.firstBrother = newNode;
        }
        newNode.firstBrother = null;
        newNode.firstChild = null;
    }

    //----------------------------
//读入食堂信息建树
    private static void buildCanteenNodesFromDatabase(){
        List<CanteenInfo> canteenInfos = LitePal.findAll(CanteenInfo.class);
        rootNode = recursiveCanteenNode(canteenInfos, 0);
    }
    //递归法建立食堂节点
    private static TreeNode recursiveCanteenNode(List<CanteenInfo> canteenInfos, int count){
        if (count == canteenInfos.size()) return null;
        NodeElement canteenNode = new NodeElement();
        canteenNode.childNumber = 0;
        canteenNode.idInDatabase = canteenInfos.get(count).getId();
        canteenNode.nodeName = canteenInfos.get(count).getCanteenName();
        TreeNode treeNode = new TreeNode(canteenNode);
        treeNode.firstChild = null;
        treeNode.firstBrother = recursiveCanteenNode(canteenInfos, count+1);
        return treeNode;
    }

    //------------------------------
//读入档口信息建树
    private static void buildWindowNodesFromDatabase(){
        List<WindowInfo> windowInfos = LitePal.findAll(WindowInfo.class);
        for (WindowInfo windowInfo: windowInfos){
            addNewWindowTreeNode(windowInfo);
        }
    }
    //判断档口是否属于该食堂
    private static boolean isWindowBelongToCanteen(WindowInfo windowInfo, TreeNode tempNode){
        return windowInfo.getBelongToCanteenName().equals(tempNode.nodeData.nodeName);
    }
    //创建档口结点的数据信息
    private static NodeElement createNewWindowNodeDataFromDatabase(WindowInfo windowInfo){
        NodeElement windowNode = new NodeElement();
        windowNode.nodeName = windowInfo.getWindowName();
        windowNode.idInDatabase = windowInfo.getId();
        windowNode.childNumber = 0;
        return windowNode;
    }
    //找到档口所属的食堂结点位置
    private static TreeNode findWindowNodePosition(WindowInfo windowInfo){
        TreeNode tempNode = rootNode; //找到新结点的父结点
        while (!isWindowBelongToCanteen(windowInfo, tempNode)){
            tempNode = tempNode.firstBrother;
            if (tempNode == null) break;//不存在该食堂
        }
        return tempNode;
    }

    //-------------------------------
//读入菜品信息建树
    private static void buildDishNodesFromDatabase(){
        List<DishInfo> dishInfos = LitePal.findAll(DishInfo.class);
        for (DishInfo dishInfo: dishInfos){
            addNewDishTreeNode(dishInfo);
        }
    }
    //判断菜品是否属于该食堂
    private static boolean isDishBelongToCanteen(DishInfo dishInfo, TreeNode tempNode){
        return dishInfo.getBelongToCanteen().equals(tempNode.nodeData.nodeName);
    }
    //判断菜品是否属于该档口
    private static boolean isDishBelongToWindow(DishInfo dishInfo, TreeNode tempNode){
        return dishInfo.getBelongToWindow().equals(tempNode.nodeData.nodeName);
    }
    //创建菜品结点的数据信息
    private static NodeElement createNewDishNodeDataFromDatabase(DishInfo dishInfo){
        NodeElement dishNode = new NodeElement();
        dishNode.idInDatabase = dishInfo.getId();
        Log.d("add dish",Integer.toString(dishNode.idInDatabase));
        dishNode.nodeName = dishInfo.getDishName();
        dishNode.childNumber = 0;
        return dishNode;
    }
    //找到菜品所属的食堂档口结点位置
    private static TreeNode findDishNodePosition(DishInfo dishInfo){
        TreeNode tempNode = rootNode; //找到新结点的父结点
        while(!isDishBelongToCanteen(dishInfo, tempNode)){
            tempNode = tempNode.firstBrother;
            if (tempNode == null) return tempNode;
        }
        tempNode = tempNode.firstChild;
        if (tempNode == null) return tempNode;
        while(!isDishBelongToWindow(dishInfo, tempNode)){
            tempNode = tempNode.firstBrother;
            if (tempNode == null) break;
        }
        return tempNode;
    }

//-----------------------------增加----------------------------------
    /**
     * 增加食堂数据操作
     * @author lixiang
     * @param canteenName 食堂名字
     * @param windowNum 下属档口数量
     * @param canteenAddress 食堂地址
     */
    public static void addNewCanteen(String canteenName, String canteenAddress, int windowNum,byte[] canteen_img){
        //向数据库增加食堂
        CanteenInfo newCanteen = new CanteenInfo();
        newCanteen.setCanteenName(canteenName);
        newCanteen.setCanteenAddress(canteenAddress);
        newCanteen.setCanteenWindowNumber(windowNum);
        newCanteen.setCanteenshot(canteen_img);
        newCanteen.save();
        //向树添加食堂结点
        addNewCanteenTreeNode(newCanteen);
    }
    //向树添加食堂结点
    private static void addNewCanteenTreeNode(CanteenInfo canteenInfo){
        TreeNode tempNode = rootNode;
        TreeNode newNode = new TreeNode(createNewCanteenNodeDataFromDatabase(canteenInfo));
        while(tempNode.firstBrother != null)
            tempNode = tempNode.firstBrother;
        tempNode.firstBrother = newNode;
        newNode.firstBrother = null;
        newNode.firstChild = null;
    }
    //创造食堂结点的数据信息
    private static NodeElement createNewCanteenNodeDataFromDatabase(CanteenInfo canteenInfo){
        NodeElement canteenNode = new NodeElement();
        canteenNode.nodeName = canteenInfo.getCanteenName();
        canteenNode.idInDatabase = canteenInfo.getId();
        canteenNode.childNumber = 0;
        return canteenNode;
    }
    /**
     * 增加档口数据操作
     * @author lixiang
     * @param windowName 档口名字
     * @param dishNum 下属菜品数量
     * @param windowAddress 档口地址
     * @param belongToCanteenName 所属食堂
     */
    public static boolean addNewWindow(String windowName, String windowAddress,
                                       String belongToCanteenName, int dishNum, byte[] window_img){
        WindowInfo newWindow = new WindowInfo();
        newWindow.setBelongToCanteenName(belongToCanteenName);
        newWindow.setWindowAddress(windowAddress);
        newWindow.setWindowDishNumber(dishNum);
        newWindow.setWindowName(windowName);
        newWindow.setWindowshot(window_img);
        newWindow.save();//存入数据库
        if(addNewWindowTreeNode(newWindow)){//若为false，则说明未找到所属食堂
            return true;
        }
        else {
            newWindow.delete();
            return false;
        }
    }
    //增加档口结点到树
    private static boolean addNewWindowTreeNode(WindowInfo windowInfo){
        TreeNode parentNode = findWindowNodePosition(windowInfo);//找到所属食堂结点
        if (parentNode == null) {
            return false;//不存在所属食堂
        }
        else {
            TreeNode newNode = new TreeNode(createNewWindowNodeDataFromDatabase(windowInfo));
            addNewNodeFromParentNode(parentNode, newNode);//加到该食堂下的子结点
            return true;
        }
    }
    /**
     * 增加菜品数据操作
     * @author lixiang
     * @param dishName 菜品名字
     * @param belongToWindowName 所属档口
     * @param price 价格
     * @param belongToCanteenName 所属食堂
     * @param tags 标签
     */
    public static boolean addNewDish(String dishName, String belongToCanteenName,
                                     String belongToWindowName, float price, String tags, byte[] dish_img){
        DishInfo newDish = new DishInfo();
        newDish.setBelongToCanteen(belongToCanteenName);
        newDish.setBelongToWindow(belongToWindowName);
        newDish.setDishName(dishName);
        newDish.setDishPrice(price);
        newDish.setDishshot(dish_img);
        newDish.setDishTags(tags);
        newDish.save();  //先save了才有分配数据库id
        if (addNewDishTreeNode(newDish)){//增加到树
            addLinkedListFromDatabase(newDish);//增加到链表
            return true;
        }
        else {
            newDish.delete();
            return false;
        }
    }
    //增加菜品结点到树
    private static boolean addNewDishTreeNode(DishInfo dishInfo){
        TreeNode parentNode = findDishNodePosition(dishInfo);
        if (parentNode == null) return false;
        else {
            TreeNode newNode = new TreeNode(createNewDishNodeDataFromDatabase(dishInfo));
            Log.d("add dish",Integer.toString(newNode.nodeData.idInDatabase));
            addNewNodeFromParentNode(parentNode, newNode);//加到该档口下的子结点
            return true;
        }
    }

//----------------------------------遍历----------------------------------
    /**
     * 遍历整棵树
     */
    public static void traversalWholeTreeNodes(){
        recursiveTraversalTreeNodes(rootNode);
    }
    //先序递归遍历
    private static void recursiveTraversalTreeNodes(TreeNode currentNode){
        if (currentNode != null){
            Log.d("TreeNode", currentNode.nodeData.nodeName);
            recursiveTraversalTreeNodes(currentNode.firstChild);
            recursiveTraversalTreeNodes(currentNode.firstBrother);
        }
    }

//------------------------------删除--------------------------------------
    /**
     * 删除菜品数据
     * @author lixiang
     * @param dishName 菜品名
     * @param belongToCanteenName 所属食堂
     * @param belongToWindowName 所属档口
     */
    public static boolean deleteDishFromDatabase(String dishName, String belongToCanteenName,
                                                 String belongToWindowName){
        //利用树存储的映射结点信息找到要删除的菜品在数据库中的ID，并在树中删除
        int positionID = deleteDishNode(dishName, belongToCanteenName, belongToWindowName);
        if (positionID == 0) {
            Log.d("delete","dishid=0");
            return false;
        }
        deleteDishFromLinkedList(positionID);//从链表中删除
        LitePal.delete(DishInfo.class, positionID);//从数据库中删除
        return true;
    }
    private static int deleteDishNode(String dishName, String belongToCanteenName,
                                      String belongToWindowName){
        TreeNode tempNode = rootNode;
        while (!tempNode.nodeData.nodeName.equals(belongToCanteenName)){
            tempNode = tempNode.firstBrother;//找到其所属食堂结点
            if (tempNode == null) return 0;
        }
        //Log.d("TreeNode", tempNode.nodeData.nodeName);
        tempNode = tempNode.firstChild;
        if (tempNode == null) return 0;
        while (!tempNode.nodeData.nodeName.equals(belongToWindowName)){
            tempNode = tempNode.firstBrother;//找到其所属档口结点
            if (tempNode == null) return 0;
        }
        tempNode.nodeData.childNumber --;
        return deleteNode(tempNode, dishName);
    }
    /**
     * 删除档口数据
     * @author lixiange
     * @param windowName 档口名
     * @param belongToCanteenName 所属食堂
     *
     */
    public static boolean deleteWindowFromDatabase(String windowName, String belongToCanteenName){
        int positionID = deleteWindowNode(windowName, belongToCanteenName);
        if (positionID == 0) return false;
        LitePal.delete(WindowInfo.class, positionID);//从数据库中删除
        LitePal.deleteAll(DishInfo.class, "belongToCanteen = ? and belongToWindow = ?", belongToCanteenName, windowName);
        createLinkedListFromDatabase();//重新生成链表
        return true;
    }
    private static int deleteWindowNode(String windowName, String belongToCanteenName){
        TreeNode tempNode = rootNode;
        while(!tempNode.nodeData.nodeName.equals(belongToCanteenName)){
            tempNode = tempNode.firstBrother;
            if (tempNode == null) return 0;
        }
        tempNode.nodeData.childNumber--;
        return deleteNode(tempNode, windowName);
    }
    /**
     * 删除食堂数据
     * @author lixiang
     * @param canteenName 食堂名
     *
     */
    public static boolean deleteCanteenFromDatabase(String canteenName){
        int positionID = deleteCanteenNode(canteenName);
        if (positionID == 0) return false;
        LitePal.delete(CanteenInfo.class, positionID);//从数据库中删除
        LitePal.deleteAll(WindowInfo.class, "belongToCanteenName = ?", canteenName);
        LitePal.deleteAll(DishInfo.class, "belongToCanteen = ?", canteenName);
        createLinkedListFromDatabase();//重新建立链表
        return true;
    }
    private static int deleteCanteenNode(String canteenName){
        TreeNode tempNode = rootNode;
        if (tempNode.nodeData.nodeName.equals(canteenName)){//根结点便是要删除的食堂
            rootNode = rootNode.firstBrother;//修改根结点
            return tempNode.nodeData.idInDatabase;
        }
        else{
            if (tempNode.firstBrother == null) return 0;
            while(!tempNode.firstBrother.nodeData.nodeName.equals(canteenName)){
                tempNode = tempNode.firstBrother;
                if (tempNode.firstBrother == null) return 0;
            }
            TreeNode deleteNode = tempNode.firstBrother;
            tempNode.firstBrother = deleteNode.firstBrother;
            return deleteNode.nodeData.idInDatabase;
        }
    }
    //通用的删除操作，并返回删除结点的ID
    private static int deleteNode(TreeNode tempNode, String name){
        if (tempNode.firstChild.nodeData.nodeName.equals(name)){//档口下第一个结点便是要删除的结点
            TreeNode deleteNode = tempNode.firstChild;
            tempNode.firstChild = deleteNode.firstBrother;//链接到第二个结点
            Log.d("delete", "is first node");
            int iid = deleteNode.nodeData.idInDatabase;
            Log.d("delete",Integer.toString(iid));
            return deleteNode.nodeData.idInDatabase;//返回在数据库中的id
        }
        else {
            tempNode = tempNode.firstChild;
            while (!tempNode.firstBrother.nodeData.nodeName.equals(name))
                tempNode = tempNode.firstBrother;
            TreeNode deleteNode = tempNode.firstBrother;
            tempNode.firstBrother = deleteNode.firstBrother;//链接到删除结点的下一个结点
            return deleteNode.nodeData.idInDatabase;//返回在数据库中的id
        }
    }

    /**
     * @author lixiang
     * -----------------------------------------嵌套链表---------------------------------------------
     */
    private static class TagHead{//标签链表
        String tagName;
        dishNode firstNode;//指向该标签下的第一个菜品结点
        TagHead nextTag;
    }
    private static class dishNode{//每个标签下所含的菜品链表
        int idInDatabase;//结点中存储菜品在数据库中的id
        dishNode nextNode;
        private dishNode(int idInDatabase){
            this.idInDatabase = idInDatabase;
        }
    }
    private static TagHead tagRoot = new TagHead();//链表根结点，指向第一个标签

    //生成嵌套链表，建立索引
    public static void createLinkedListFromDatabase(){
        List<DishInfo> dishInfos = LitePal.findAll(DishInfo.class);
        for (DishInfo dishInfo: dishInfos){//菜品数据库中的每一道菜品
            addLinkedListFromDatabase(dishInfo);
        }
    }
    //从数据库信息添加到链表
    private static void addLinkedListFromDatabase(DishInfo dishInfo){
        int dishID = dishInfo.getId();
        String[] dishTags = dishInfo.getDishTags().split("\\$");
        for (String dishTag: dishTags){ //该菜品下的每个标签
            TagHead targetTag = findTagPosition(dishTag);//找到该标签在链表中的位置
            addDishToTag(targetTag, dishID);//将该菜品添加到该标签的链表后
        }
    }
    //从标签链表中找到指定标签,若不存在，则增加新标签
    private static TagHead findTagPosition(String tag){
        TagHead tempNode = tagRoot;
        if (tempNode.tagName == null){//还没有根结点
            tempNode.tagName = tag;
            return tagRoot;
        }
        else {
            if (tempNode.tagName.equals(tag))//只有一个结点即该标签
                return tempNode;
            while (tempNode.nextTag != null){//从已有的所有标签找
                if (tempNode.nextTag.tagName.equals(tag))
                    return tempNode.nextTag;
                else tempNode = tempNode.nextTag;
            }
            //不在已有的标签中，增加新标签
            TagHead newTagNode = new TagHead();
            tempNode.nextTag = newTagNode;
            newTagNode.tagName = tag;
            return newTagNode;
        }
    }
    //在标签后的链表中添加该菜品建立索引
    private static void addDishToTag(TagHead targetTag, int dishID){
        dishNode newDishNode = new dishNode(dishID);
        if (targetTag.firstNode == null){//该标签链表下还没有菜品
            targetTag.firstNode = newDishNode;
        }
        else {
            dishNode tempNode = targetTag.firstNode;
            while (tempNode.nextNode != null)//直到链表尾，添加菜品结点
                tempNode = tempNode.nextNode;
            tempNode.nextNode = newDishNode;
        }
    }

    //删除菜品的标签下其链表结点
    private static void deleteDishFromLinkedList(int positionID){
        DishInfo dishInfo = LitePal.find(DishInfo.class, positionID);
        String[] dishTags = dishInfo.getDishTags().split("\\$");
        for (String dishTag: dishTags){ //该菜品下的每个标签
            TagHead targetTag = tagRoot;
            dishNode targetDish;
            while (!targetTag.tagName.equals(dishTag))//找到该标签位置
                targetTag = targetTag.nextTag;
            targetDish = targetTag.firstNode;
            if (targetDish.idInDatabase == dishInfo.getId()){//标签下的第一个结点
                targetTag.firstNode = targetDish.nextNode;
            }
            else{
                while (targetDish.nextNode.idInDatabase != dishInfo.getId())
                    targetDish = targetDish.nextNode;
                dishNode deleteDish = targetDish.nextNode;//改变指针，删除菜品结点
                targetDish.nextNode = deleteDish.nextNode;
            }
        }
    }

    //遍历输出嵌套链表
    public static List<Integer> traversalWholeLinkedList(){
        TagHead tempTagNode = tagRoot;
        List<Integer> perId=new ArrayList<>();
        while (tempTagNode != null){
            dishNode tempDishNode = tempTagNode.firstNode;
            Log.d("LinkedList", tempTagNode.tagName);
            while (tempDishNode != null){
                if(!perId.contains(tempDishNode.idInDatabase)){
                    perId.add(tempDishNode.idInDatabase);
                }
                Log.d("LinkedList", String.valueOf(tempDishNode.idInDatabase));
                tempDishNode = tempDishNode.nextNode;
            }
            tempTagNode = tempTagNode.nextTag;
        }
        return perId;
    }
    public static List<Integer> getUnitedList(List<String> tag){
        //求tag的并集
        List<Integer> perId=new ArrayList<>();
        TagHead tempTagNode=tagRoot;
        while(tempTagNode!=null){
            Log.d("United", tempTagNode.tagName);
            for(int i=0;i<tag.size();i++)
            {
                if(tag.get(i).equals(tempTagNode.tagName)){
                    dishNode tempDishNode = tempTagNode.firstNode;
                    while (tempDishNode != null){
                        if(!perId.contains(tempDishNode.idInDatabase)){
                            perId.add(tempDishNode.idInDatabase);
                            Log.d("United", String.valueOf(tempDishNode.idInDatabase));
                        }
                        tempDishNode = tempDishNode.nextNode;
                    }
                }
            }
            tempTagNode = tempTagNode.nextTag;
        }

        return perId;

    }

    /**
     * ---------------------------------------队列---------------------------------------------------
     * @author Lily
     * 队列基本操作
     */
    public static class Queue {
        private List<dish> data;
        private int front;// 队列头，允许删除
        private int rear;// 队列尾，允许插入

        public Queue() {
            data=new ArrayList<>();
            front = rear = 0;
        }

        // 入列一个元素
        public void offer(dish e) {
            data.add(e);
            rear++;
        }

        // 返回队首元素，但不删除
        public dish peek() {
            return (dish) data.get(front);
        }

        // 扫描队列中的元素
        public dish display(int i){
            return (dish) data.get(i);
        }

        // 出队排在最前面的一个元素
        public dish poll() {
            dish value = data.get(front);// 保留队列的front端的元素的值
            data.remove(front);
            front++;// 释放队列的front端的元素
            return value;
        }

    }


    /**
     * ---------------------------------------评分---------------------------------------------------
     * @author lixiang
     * 计算评分，从菜品评分计算档口评分和食堂评分
     * 算法：累加每个结点后的兄弟结点分数，食堂评分为档口评分平均值，档口评分为菜品评分平均值
     */
    public static void addDishScore(String belongToCanteenName, String belongToWindowName,
                                    String dishName, float score){
        float newScore;
        newScore = findAndChangeDishNodeScore(rootNode, belongToCanteenName, belongToWindowName,
                dishName, score);

    }

    //递归找到菜品在树中的位置，并在递归返回时将菜品结点及对应的档口、食堂结点的评分重新计算修改
    private static float findAndChangeDishNodeScore(TreeNode tempNode, String belongToCanteenName,
                                                    String belongToWindowName, String dishName, float score){
        float returnScore;
        Log.d("Score dish:", dishName);
        if (tempNode.nodeData.nodeName.equals(dishName)){//找到该菜品结点
            returnScore = detailChangeDishScoreInTreeAndDatabase(tempNode, score);
        }
        else if (tempNode.nodeData.nodeName.equals(belongToCanteenName)){//找到对应食堂
            float changeScore = findAndChangeDishNodeScore(tempNode.firstChild,
                    belongToCanteenName, belongToWindowName, dishName, score);
            returnScore = detailChangeCanteenScoreInTreeAndDatabase(tempNode, changeScore);

        }
        else if (tempNode.nodeData.nodeName.equals(belongToWindowName)){//找到对应档口
            float changeScore = findAndChangeDishNodeScore(tempNode.firstChild,
                    belongToCanteenName, belongToWindowName, dishName, score);
            returnScore = detailChangeWindowScoreInTreeAndDatabase(tempNode, changeScore);
        }
        else {//在菜品/档口/食堂兄弟结点中寻找
            float changeScore = findAndChangeDishNodeScore(tempNode.firstBrother, belongToCanteenName,
                    belongToWindowName, dishName, score);
            tempNode.nodeData.sumScore = tempNode.nodeData.score + changeScore;
            returnScore = tempNode.nodeData.sumScore;
        }
        return returnScore;
    }

    //计算新的结点单评分
    private static float calculateNewScore(TreeNode tempNode, float score){
        float number = tempNode.nodeData.childNumber;
        float previousScore = tempNode.nodeData.score;
        return (previousScore * (number - 1) + score) / number;
    }

    //计算变动结点的累加评分
    private static float calculateNewSumScore(TreeNode tempNode, float newScore){
        float previousSumScore = tempNode.nodeData.sumScore;
        float previousScore = tempNode.nodeData.score;
        float newSumScore = previousSumScore - previousScore + newScore;
        tempNode.nodeData.sumScore = newSumScore;
        return newSumScore;
    }

    //计算菜品新的评分并更新到数据库
    private static float detailChangeDishScoreInTreeAndDatabase(TreeNode tempNode, float score){
        tempNode.nodeData.childNumber ++;//用childNumber存评分的人数
        float newScore = calculateNewScore(tempNode, score);//重新计算评分平均分
        float newSumScore = calculateNewSumScore(tempNode, newScore);//计算累加评分
        tempNode.nodeData.score = newScore;
        int positionID = tempNode.nodeData.idInDatabase;
        DishInfo dishInfo = LitePal.find(DishInfo.class, positionID);
        dishInfo.setDishScore(newScore);
        dishInfo.save();
        return newSumScore;//返回累加评分
    }

    //计算食堂评分并更新到数据库
    private static float detailChangeCanteenScoreInTreeAndDatabase(TreeNode tempNode, float score){
        float newScore = score / tempNode.nodeData.childNumber;//计算食堂新评分
        tempNode.nodeData.score = newScore;
        int positionID = tempNode.nodeData.idInDatabase;
        CanteenInfo canteenInfo = LitePal.find(CanteenInfo.class, positionID);
        canteenInfo.setCanteenScore(newScore);
        canteenInfo.save();
        return newScore;
    }

    //计算档口评分并更新到数据库
    private static float detailChangeWindowScoreInTreeAndDatabase(TreeNode tempNode, float score){
        float newScore = score / tempNode.nodeData.childNumber;//计算档口新评分
        float newSumScore = calculateNewSumScore(tempNode, newScore);//计算累加评分
        tempNode.nodeData.score = newScore;
        int positionID = tempNode.nodeData.idInDatabase;
        WindowInfo windowInfo = LitePal.find(WindowInfo.class, positionID);
        windowInfo.setWindowScore(newScore);
        windowInfo.save();
        return newSumScore;
    }
}
