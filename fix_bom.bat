@echo off
cd /d "%~dp0"

echo 正在移除 Book*.java 文件中的 UTF-8 BOM ...
for %%f in (Book*.java) do (
    powershell -NoProfile -Command ^
        "$content = Get-Content -Path '%%f' -Raw -Encoding UTF8;" ^
        "$utf8NoBom = New-Object System.Text.UTF8Encoding($false);" ^
        "[System.IO.File]::WriteAllText((Resolve-Path '%%f'), $content, $utf8NoBom)"
    echo 已修复: %%f
)
echo 完成！所有文件已保存为无 BOM 的 UTF-8。
pause