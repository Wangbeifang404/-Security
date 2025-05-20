<?php
/**
 * Created by runner.han
 * There is nothing new under the sun
 */


$SELF_PAGE = substr($_SERVER['PHP_SELF'],strrpos($_SERVER['PHP_SELF'],'/')+1);

if ($SELF_PAGE == "xxe_1.php"){
    $ACTIVE = array('','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','active open','','active','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','');
}

$PIKA_ROOT_DIR =  "../../";
include_once $PIKA_ROOT_DIR.'header.php';


//payload,url编码一下:
$xxepayload1 = <<<EOF
<?xml version = "1.0"?>
<!DOCTYPE ANY [
    <!ENTITY f SYSTEM "file:///etc/passwd">
]>
<x>&f;</x>
EOF;

$xxetest = <<<EOF
<?xml version = "1.0"?>
<!DOCTYPE note [
    <!ENTITY hacker "ESHLkangi">
]>
<name>&hacker;</name>
EOF;

//$xxedata = simplexml_load_string($xxetest,'SimpleXMLElement');
//print_r($xxedata);


//查看当前LIBXML的版本
//print_r(LIBXML_VERSION);

$html='';
if(isset($_POST['submit']) and $_POST['xml'] != null){
    $xml = $_POST['xml'];
    
    // 创建安全的XML解析上下文
    $dom = new DOMDocument();
    $libxmlPreviousState = libxml_disable_entity_loader(true); // 禁用外部实体加载
    
    try {
        if ($dom->loadXML($xml, LIBXML_NOENT | LIBXML_DTDLOAD)) {
            $data = simplexml_import_dom($dom);
            if ($data) {
                $html .= "<pre>{$data}</pre>";
            } else {
                $html .= "<p>XML解析成功，但转换为SimpleXML对象失败</p>";
            }
        } else {
            $html .= "<p>XML格式不正确，请检查语法</p>";
        }
    } catch (Exception $e) {
        $html .= "<p>处理XML时发生错误: " . htmlspecialchars($e->getMessage()) . "</p>";
    } finally {
        libxml_disable_entity_loader($libxmlPreviousState); // 恢复之前的libxml状态
    }
}

?>


<div class="main-content">
    <div class="main-content-inner">
        <div class="breadcrumbs ace-save-state" id="breadcrumbs">
            <ul class="breadcrumb">
                <li>
                    <i class="ace-icon fa fa-home home-icon"></i>
                    <a href="xee.php"></a>
                </li>
                <li class="active">xxe漏洞</li>
            </ul>
            <a href="#" style="float:right" data-container="body" data-toggle="popover" data-placement="bottom" title="tips(再点一下关闭)"
               data-content="先把XML声明、DTD文档类型定义、文档元素这些基础知识自己看一下">
                点一下提示~
            </a>
        </div>
        <div class="page-content">

            <form method="post">
                <p>这是一个接收xml数据的api:</p>
                    <input type="text" name="xml" />
                    <input type="submit" name="submit" value="提交">
            </form>
            <?php echo $html;?>


        </div><!-- /.page-content -->
    </div>
</div><!-- /.main-content -->



<?php
include_once $PIKA_ROOT_DIR . 'footer.php';

?>