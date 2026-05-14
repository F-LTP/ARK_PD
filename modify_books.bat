@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"

echo 正在处理以 Book 开头的 .java 文件（UTF-8 无 BOM）...
for %%f in (Book*.java) do (
    echo 修改: %%f
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
        "$file='%%f';" ^
        "$lines=Get-Content -Encoding UTF8 $file;" ^
        "$new=New-Object System.Collections.Generic.List[string];" ^
        "for($i=0;$i -lt $lines.Count;$i++){" ^
            "$line=$lines[$i];" ^
            "$new.Add($line);" ^
            "$trim=$line.Trim();" ^
            "if($trim -eq 'import com.shatteredpixel.shatteredpixeldungeon.items.Item;'){" ^
                "$indent=$line -replace '^(\s*).*$','$1';" ^
                "$next=if($i+1 -lt $lines.Count){$lines[$i+1].Trim()}else{''};" ^
                "$add='import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;';" ^
                "if($next -ne $add){$new.Add($indent+$add)}" ^
            "}" ^
            "if($trim -eq 'detach(hero.belongings.backpack);'){" ^
                "$indent=$line -replace '^(\s*).*$','$1';" ^
                "$next=if($i+1 -lt $lines.Count){$lines[$i+1].Trim()}else{''};" ^
                "$add='Catalog.countUse(getClass());';" ^
                "if($next -ne $add){$new.Add($indent+$add)}" ^
            "}" ^
        "}" ^
        "$utf8NoBom=New-Object System.Text.UTF8Encoding(`$false);" ^
        "[System.IO.File]::WriteAllLines((Resolve-Path $file), $new, $utf8NoBom)"
)
echo 完成！所有文件已用无 BOM 的 UTF-8 保存。
pause