Add-Type -AssemblyName System.Drawing
$img = [System.Drawing.Image]::FromFile("C:\Users\Admin\Desktop\News APK\6FFB08E9.jpg")

$adaptiveSizes = @{ "mdpi" = 108; "hdpi" = 162; "xhdpi" = 216; "xxhdpi" = 324; "xxxhdpi" = 432 }
$legacySizes = @{ "mdpi" = 48; "hdpi" = 72; "xhdpi" = 96; "xxhdpi" = 144; "xxxhdpi" = 192 }

foreach ($key in $adaptiveSizes.Keys) {
    $size = $adaptiveSizes[$key]
    $bmp = New-Object System.Drawing.Bitmap($size, $size)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.DrawImage($img, 0, 0, $size, $size)
    
    $path = "C:\Users\Admin\Desktop\News APK\app\src\main\res\mipmap-$key\ic_launcher_foreground.png"
    $bmp.Save($path, [System.Drawing.Imaging.ImageFormat]::Png)
    $g.Dispose()
    $bmp.Dispose()
}

foreach ($key in $legacySizes.Keys) {
    $size = $legacySizes[$key]
    $bmp = New-Object System.Drawing.Bitmap($size, $size)
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.DrawImage($img, 0, 0, $size, $size)
    
    $pathIcon = "C:\Users\Admin\Desktop\News APK\app\src\main\res\mipmap-$key\ic_launcher.png"
    $bmp.Save($pathIcon, [System.Drawing.Imaging.ImageFormat]::Png)
    $pathRound = "C:\Users\Admin\Desktop\News APK\app\src\main\res\mipmap-$key\ic_launcher_round.png"
    $bmp.Save($pathRound, [System.Drawing.Imaging.ImageFormat]::Png)
    
    $g.Dispose()
    $bmp.Dispose()
}

$img.Dispose()

# Delete old webp files
Get-ChildItem -Path "C:\Users\Admin\Desktop\News APK\app\src\main\res" -Recurse -Filter "ic_launcher*.webp" | Remove-Item -Force
