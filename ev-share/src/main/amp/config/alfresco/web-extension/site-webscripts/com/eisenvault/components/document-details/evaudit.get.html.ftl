<#assign el=args.htmlid?js_string/>
<div class="document-details-panel">
  <h2 id="${el}-heading" class="thin dark">${msg("heading")}</h2>
  <div>${msg("text")}</div>
  <script type="text/javascript">//<![CDATA[
    Alfresco.util.createTwister("${el}-heading", "DocumentAudit");
  //]]></script>
</div>