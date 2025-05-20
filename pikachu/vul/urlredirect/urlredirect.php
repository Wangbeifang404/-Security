<?php
/**
 * Created by runner.han
 * There is nothing new under the sun
 */

$SELF_PAGE = substr($_SERVER['PHP_SELF'], strrpos($_SERVER['PHP_SELF'], '/') + 1);

// 修正条件判断，使用 === 进行比较
if ($SELF_PAGE === "urlredirect.php") {
    $ACTIVE = array('','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','active open','','active','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','');
}

$PIKA_ROOT_DIR =  "../../";
include_once $PIKA_ROOT_DIR.'header.php';

$html = "";
if (isset($_GET['url']) && $_GET['url'] != null) {
    $url = $_GET['url'];
    if ($url == 'i') {
        $html .= "<p>好的,希望你能坚持做你自己!</p>";
    } else {
        // 白名单验证，只允许跳转到指定的安全域名
        $allowed_domains = array('example.com', 'example2.com'); // 替换为实际的安全域名
        $parsed_url = parse_url($url);
        if (isset($parsed_url['host']) && in_array($parsed_url['host'], $allowed_domains)) {
            header("location:{$url}");
        } else {
            $html .= "<p>不允许的跳转地址，请使用安全的链接。</p>";
        }
    }
}
?>

<div class="main-content">
    <div class="main-content-inner">
        <div class="breadcrumbs ace-save-state" id="breadcrumbs">
            <ul class="breadcrumb">
                <li>
                    <i class="ace-icon fa fa-home home-icon"></i>
                    <a href="unsafere.php"></a> <!-- 修正链接错误，去掉多余的 .php -->
                </li>
                <li class="active">不安全的url跳转</li>
            </ul>
            <a href="#" style="float:right" data-container="body" data-toggle="popover" data-placement="bottom" title="tips(再点一下关闭)"
               data-content="仔细看下每个请求的内容">
                点一下提示~
            </a>
        </div>
        <div class="page-content">
            <div class="vul info">
                我想问一下,你到底是下面哪一种类型的人:<br>
                <pre>
                    <a href="urlredirect.php">像春天的花一样的少年</a>
                    <a href="urlredirect.php">像夏天的雨一样的少年</a>
                    <a href="urlredirect.php?url=unsafere.php">像秋天的风一样的少年</a>
                    <a href="urlredirect.php?url=i">我就是我,放荡不羁的我</a>
                </pre>
                <?php echo $html;?>
            </div>
        </div><!-- /.page-content -->
    </div>
</div><!-- /.main-content -->

<?php
include_once $PIKA_ROOT_DIR . 'footer.php';
?>