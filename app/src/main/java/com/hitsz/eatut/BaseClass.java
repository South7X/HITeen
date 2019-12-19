package com.hitsz.eatut;

import android.util.Log;
import android.widget.TextView;

import com.hitsz.eatut.database.CanteenInfo;
import com.hitsz.eatut.database.DishInfo;
import com.hitsz.eatut.database.WindowInfo;

import org.litepal.LitePal;

import java.util.List;

public class BaseClass {

    /**
     * -------------------------------哈希部分------------------------------------
     */

    /**
     * 函数功能：折叠哈希函数法通过用户手机号计算哈希Key值
     * @author  lixiang
     * @param userTelephone 用户手机号码
     * @param maxUserNum 用户数量
     * @return 返回计算得到的hash值
     */
    public static int getHashCodeByTelephone(long userTelephone, int maxUserNum){
        long currentNumber = userTelephone;
        long modResult;
        int hashCodeResult = 0;
        while (currentNumber > maxUserNum){     //循环除以100000
            modResult = currentNumber % maxUserNum;
            currentNumber = currentNumber / maxUserNum;
            hashCodeResult += (int)modResult;   //取余数，实现五位数折叠相加
        }
        hashCodeResult += (int)currentNumber;   //加上最后一位
        Log.d("HashCode", userTelephone + ":" + String.valueOf(hashCodeResult%maxUserNum));
        return (hashCodeResult % maxUserNum);   //取余，防止超出最大用户数
    }


    /**
     * -------------------------------树部分---------------------------------------
     */
    //存储结点信息
    private static class NodeElement{
        String nodeName;        //名称（食堂、档口、菜品）
        float score;            //评分
        int childNumber;        //下属级别数量
        int idInDatabase;       //所在数据库中的id
    }

    //左孩子右兄弟表示法存储树的结构
    private static class TreeNode{
        NodeElement nodeData;   //结点数据
        TreeNode firstChild;    //第一个孩子
        TreeNode firstBrother;  //第一个兄弟
        private TreeNode(NodeElement nodeData){
            this.nodeData = nodeData;
        }
    }

    private static TreeNode rootNode;

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
        dishNode.nodeName = dishInfo.getDishName();
        dishNode.childNumber = 0;
        return dishNode;
    }
    //找到菜品所属的食堂档口结点位置
    private static TreeNode findDishNodePosition(DishInfo dishInfo){
        TreeNode tempNode = rootNode; //找到新结点的父结点
        while(!isDishBelongToCanteen(dishInfo, tempNode)){
            tempNode = tempNode.firstBrother;
        }
        tempNode = tempNode.firstChild;
        while(!isDishBelongToWindow(dishInfo, tempNode)){
            tempNode = tempNode.firstBrother;
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
    public static void addNewCanteen(String canteenName, String canteenAddress, int windowNum){
        //向数据库增加食堂
        CanteenInfo newCanteen = new CanteenInfo();
        newCanteen.setCanteenName(canteenName);
        newCanteen.setCanteenAddress(canteenAddress);
        newCanteen.setCanteenWindowNumber(windowNum);
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
    public static void addNewWindow(String windowName, String windowAddress,
                                    String belongToCanteenName, int dishNum){
        WindowInfo newWindow = new WindowInfo();
        newWindow.setBelongToCanteenName(belongToCanteenName);
        newWindow.setWindowAddress(windowAddress);
        newWindow.setWindowDishNumber(dishNum);
        newWindow.setWindowName(windowName);
        newWindow.save();
        addNewWindowTreeNode(newWindow);
    }
    //增加档口结点到树
    private static void addNewWindowTreeNode(WindowInfo windowInfo){
        TreeNode newNode = new TreeNode(createNewWindowNodeDataFromDatabase(windowInfo));
        TreeNode parentNode = findWindowNodePosition(windowInfo);
        addNewNodeFromParentNode(parentNode, newNode);//加到该食堂下的子结点
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
    public static void addNewDish(String dishName, String belongToCanteenName,
                                  String belongToWindowName, float price, String tags){
        DishInfo newDish = new DishInfo();
        newDish.setBelongToCanteen(belongToCanteenName);
        newDish.setBelongToWindow(belongToWindowName);
        newDish.setDishName(dishName);
        newDish.setDishPrice(price);
        newDish.setDishTags(tags);
        newDish.save();
        addNewDishTreeNode(newDish);
        addLinkedListFromDatabase(newDish);
    }
    //增加菜品结点到树
    private static void addNewDishTreeNode(DishInfo dishInfo){
        TreeNode newNode = new TreeNode(createNewDishNodeDataFromDatabase(dishInfo));
        TreeNode parentNode = findDishNodePosition(dishInfo);
        addNewNodeFromParentNode(parentNode, newNode);//加到该档口下的子结点
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
    public static void deleteDishFromDatabase(String dishName, String belongToCanteenName,
                                              String belongToWindowName){
        //利用树存储的映射结点信息找到要删除的菜品在数据库中的ID，并在树中删除
        int positionID = deleteDishNode(dishName, belongToCanteenName, belongToWindowName);
        deleteDishFromLinkedList(positionID);//从链表中删除
        LitePal.delete(DishInfo.class, positionID);//从数据库中删除
    }
    private static int deleteDishNode(String dishName, String belongToCanteenName,
                                       String belongToWindowName){
        TreeNode tempNode = rootNode;
        while (!tempNode.nodeData.nodeName.equals(belongToCanteenName))
            tempNode = tempNode.firstBrother;//找到其所属食堂结点
        //Log.d("TreeNode", tempNode.nodeData.nodeName);
        tempNode = tempNode.firstChild;
        while (!tempNode.nodeData.nodeName.equals(belongToWindowName))
            tempNode = tempNode.firstBrother;//找到其所属档口结点
        tempNode.nodeData.childNumber --;
        return deleteNode(tempNode, dishName);
    }
    /**
     * 删除档口数据
     * @author lixiange
     * @param windowName 档口名
     * @param belongToCanteenName 所属食堂
     */
    public static void deleteWindowFromDatabase(String windowName, String belongToCanteenName){
        int positionID = deleteWindowNode(windowName, belongToCanteenName);
        LitePal.delete(WindowInfo.class, positionID);//从数据库中删除
    }
    private static int deleteWindowNode(String windowName, String belongToCanteenName){
        TreeNode tempNode = rootNode;
        while(!tempNode.nodeData.nodeName.equals(belongToCanteenName))
            tempNode = tempNode.firstBrother;
        tempNode.nodeData.childNumber--;
        return deleteNode(tempNode, windowName);
    }
    /**
     * 删除食堂数据
     * @author lixiange
     * @param canteenName 食堂名
     */
    public static void deleteCanteenFromDatabase(String canteenName){
        int positionID = deleteCanteenNode(canteenName);
        LitePal.delete(CanteenInfo.class, positionID);//从数据库中删除
    }
    private static int deleteCanteenNode(String canteenName){
        TreeNode tempNode = rootNode;
        if (tempNode.nodeData.nodeName.equals(canteenName)){//根结点便是要删除的食堂
            rootNode = rootNode.firstBrother;//修改根结点
            return tempNode.nodeData.idInDatabase;
        }
        else{
            while(!tempNode.firstBrother.nodeData.nodeName.equals(canteenName))
                tempNode = tempNode.firstBrother;
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

    //-----------------------------修改-----------------------------------
    //TODO:write update function

    /**
     * ----------------------------------嵌套链表---------------------------------------
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

    //TODO：删除链表结点
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
    public static void traversalWholeLinkedList(){
        TagHead tempTagNode = tagRoot;
        while (tempTagNode != null){
            dishNode tempDishNode = tempTagNode.firstNode;
            Log.d("LinkedList", tempTagNode.tagName);
            while (tempDishNode != null){
                Log.d("LinkedList", String.valueOf(tempDishNode.idInDatabase));
                tempDishNode = tempDishNode.nextNode;
            }
            tempTagNode = tempTagNode.nextTag;
        }
    }

    /**
     * 计算评分，从菜品评分计算档口评分和食堂评分
     * TODO：
     */

}
